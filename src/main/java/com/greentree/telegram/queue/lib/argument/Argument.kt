package com.greentree.telegram.queue.lib.argument

import com.greentree.commons.annotation.Annotations
import com.greentree.commons.reflection.info.TypeInfo
import com.greentree.commons.reflection.info.TypeInfoBuilder.getTypeInfo
import com.greentree.commons.reflection.info.TypeUtil.*
import org.springframework.beans.factory.annotation.Qualifier
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Field
import java.lang.reflect.Member
import java.lang.reflect.Parameter

interface Argument : AnnotatedElement {

	fun isSupportedType(type: TypeInfo<*>): Boolean = isExtends(this.type, type)

	val name: String
	val type: TypeInfo<*>

	companion object {

		fun of(field: Field) = FieldArgument(field)
		fun of(parameter: Parameter) = ParameterArgument(parameter)
	}
}

fun Argument.isSupportedType(cls: Class<*>) = isSupportedType(getTypeInfo(cls))

data class FieldArgument(val field: Field) : Argument, AnnotatedElement by Annotations.filter(field) {

	override val name: String
		get() = this.field.qualifierOrName
	override val type: TypeInfo<*>
		get() = getTypeInfo<Any>(this.field.genericType)
}

data class ParameterArgument(val parameter: Parameter) : Argument, AnnotatedElement by Annotations.filter(parameter) {

	override val name: String
		get() = this.parameter.qualifierOrName
	override val type: TypeInfo<*>
		get() = getTypeInfo<Any>(this.parameter.parameterizedType)
}

data class TypeArgument(
	override val name: String,
	override val type: TypeInfo<*>,
) : Argument {

	override fun <T : Annotation?> getAnnotation(annotationClass: Class<T>): T? = null

	override fun getAnnotations(): Array<Annotation> = arrayOf()

	override fun getDeclaredAnnotations(): Array<Annotation> = arrayOf()
}

val Parameter.qualifierOrName: String
	get() = Annotations.filter(this).getAnnotation(Qualifier::class.java)?.value ?: run {
//		require(isNamePresent) { "parameter $this is not present" }
		name
	}
val <T> T.qualifierOrName: String where T : AnnotatedElement, T : Member
	get() = Annotations.filter(this).getAnnotation(Qualifier::class.java)?.value ?: name