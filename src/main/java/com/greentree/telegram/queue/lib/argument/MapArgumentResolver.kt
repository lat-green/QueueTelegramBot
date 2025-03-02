package com.greentree.telegram.queue.lib.argument

import com.greentree.commons.annotation.Annotations
import com.greentree.commons.reflection.info.TypeInfo
import com.greentree.telegram.queue.lib.argument.MapArgumentResolver.*
import org.springframework.beans.factory.annotation.Qualifier

data class MapArgumentResolver(
	val params: Map<String, Any>,
) : ArgumentResolver {

	private val converter = MultiTypeConverter(
		SelfTypeConverter,
		NotThrowTypeConverter { (it as String).toLong() },
		NotThrowTypeConverter { (it as String).toInt() },
	)

	override fun resolveArgument(argument: Argument): Any {
		val type = argument.type.boxing
		val qualifier = Annotations.filter(argument).getAnnotation(Qualifier::class.java)?.value
		if(qualifier != null)
			return converter.convert(type, params[qualifier] ?: TODO("$argument"))!!
		return params.values.filter { converter.supports(type, it) }
			.map { converter.convert(type, it) }
			.first()!!
	}

	override fun supportsArgument(argument: Argument): Boolean {
		val type = argument.type.boxing
		val qualifier = Annotations.filter(argument).getAnnotation(Qualifier::class.java)?.value
		if(qualifier != null)
			return converter.supports(type, params[qualifier] ?: return false)
		return params.values.any { converter.supports(type, it) }
	}

	interface TypeConverter {

		fun supports(type: TypeInfo<*>, value: Any): Boolean
		fun <T> convert(type: TypeInfo<T>, value: Any): T
	}

	data class MultiTypeConverter(val converters: Sequence<TypeConverter>) : TypeConverter {

		constructor(vararg converters: TypeConverter) : this(sequenceOf(*converters))

		override fun supports(type: TypeInfo<*>, value: Any) = converters.any { it.supports(type, value) }

		override fun <T> convert(type: TypeInfo<T>, value: Any): T = converters
			.filter { it.supports(type, value) }
			.map { it.convert(type, value) }
			.first()
	}

	fun interface NotThrowTypeConverter : TypeConverter {

		override fun supports(type: TypeInfo<*>, value: Any): Boolean = try {
			type.isInstance(convertOrThrow(value))
		} catch(e: Throwable) {
			false
		}

		override fun <T> convert(type: TypeInfo<T>, value: Any) = convertOrThrow(value) as T

		fun convertOrThrow(value: Any): Any
	}

	data object SelfTypeConverter : TypeConverter {

		override fun <T> convert(type: TypeInfo<T>, value: Any): T = value as T

		override fun supports(type: TypeInfo<*>, value: Any): Boolean = type.isInstance(value)
	}
}