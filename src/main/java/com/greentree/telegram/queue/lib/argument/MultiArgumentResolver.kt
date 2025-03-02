package com.greentree.telegram.queue.lib.argument

class MultiArgumentResolver(
	private val resolvers: List<ArgumentResolver>,
) : ArgumentResolver {

	override fun supportsArgument(argument: Argument) = resolvers.any { it.supportsArgument(argument) }

	override fun resolveArgument(argument: Argument) = resolvers.filter {
		it.supportsArgument(argument)
	}.map {
		it.resolveArgument(argument)
	}.firstOrNull()
}