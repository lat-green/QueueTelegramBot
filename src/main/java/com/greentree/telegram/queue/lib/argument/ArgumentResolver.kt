package com.greentree.telegram.queue.lib.argument

import java.lang.reflect.Field
import java.lang.reflect.Parameter

interface ArgumentResolver {

	fun supportsArgument(argument: Argument): Boolean
	fun resolveArgument(argument: Argument): Any?
}

fun ArgumentResolver.resolveArgument(argument: Parameter) = resolveArgument(ParameterArgument(argument))
fun ArgumentResolver.resolveArgument(argument: Field) = resolveArgument(FieldArgument(argument))
