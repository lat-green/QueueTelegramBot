package com.greentree.telegram.queue.lib.argument

import com.greentree.commons.reflection.info.TypeInfoBuilder.getTypeInfo
import org.springframework.stereotype.Component
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.Parameter

@Component
class MethodCaller(
	resolvers: List<ArgumentResolver>,
) {

	private val resolver = MultiArgumentResolver(resolvers)

	fun <T> call(constructor: Constructor<T>): T {
		require(constructor.trySetAccessible()) { "$constructor not accessible" }
		return constructor.newInstance(*Array(constructor.parameterCount) { index ->
			resolve(constructor.parameters[index])
		})
	}

	fun callStatic(
		method: Method,
	): Any {
		require(method.trySetAccessible()) { "$method not accessible" }
		val result = method.invoke(null, *Array(method.parameterCount) { index ->
			resolve(method.parameters[index])
		})
		if(result == null && method.returnType == Void.TYPE)
			return Unit
		return result
	}

	fun call(
		method: Method,
		thisRef: Any,
	): Any? {
		require(method.trySetAccessible()) { "$method not accessible" }
		val result: Any? = try {
			method.invoke(thisRef, *Array(method.parameterCount) { index ->
				resolve(method.parameters[index])
			})
		} catch(e: InvocationTargetException) {
			throw e.cause!!
		}
		if(result == null && method.returnType == Void.TYPE)
			return Unit
		return result
	}

	fun call(
		method: Method,
	): Any? {
		val thisRef = resolve(TypeArgument("thisRef", getTypeInfo(method.declaringClass)))
			?: throw NullPointerException("thisRef for $method not found")
		return call(method, thisRef)
	}

	fun isSupports(method: Method) = method.parameters.all { isSupports(it) }
	fun isSupports(constructor: Constructor<*>) = constructor.parameters.all {
		isSupports(it)
	}

	fun setField(
		field: Field,
		thisRef: Any,
	) {
		require(field.trySetAccessible()) { "$field not accessible" }
		val value = resolve(field)
		field.set(thisRef, value)
	}

	fun resolve(
		field: Field,
	) = resolver.resolveArgument(field)

	fun resolve(
		parameter: Parameter,
	) = resolver.resolveArgument(parameter)

	fun resolve(
		argument: Argument,
	) = resolver.resolveArgument(argument)

	fun builder() = Builder(resolver)

	fun isSupports(field: Field) = isSupports(Argument.of(field))
	fun isSupports(parameter: Parameter) = isSupports(Argument.of(parameter))

	fun isSupports(argument: Argument) = resolver.supportsArgument(argument)

	fun <T> newInstance(type: Class<T>): T = ((type as Class<Any>).kotlin.objectInstance as T) ?: run {
		val constructors = type.constructors.filter { constructor ->
			isSupports(constructor)
		}
		require(constructors.isNotEmpty()) {
			"$type not support constructors parameters ${
				type.constructors.flatMap { const ->
					const.parameters.filter {
						!isSupports(it)
					}
				}
			}"
		}
		val constructor = constructors.maxBy { it.parameterCount } as Constructor<out T>
		return call(constructor)
	}

	class Builder(vararg resolvers: ArgumentResolver) {

		private val resolvers = mutableListOf<ArgumentResolver>()

		init {
			resolvers.forEach {
				this.resolvers.add(it)
			}
		}

		fun add(resolver: ArgumentResolver): Builder {
			resolvers.add(resolver)
			return this
		}

		fun build() = MethodCaller(resolvers)
	}
}