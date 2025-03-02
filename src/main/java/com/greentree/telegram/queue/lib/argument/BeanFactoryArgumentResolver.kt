package com.greentree.telegram.queue.lib.argument

import com.greentree.commons.annotation.Annotations
import org.springframework.beans.factory.BeanFactory
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
class BeanFactoryArgumentResolver(
	private val beanFactory: BeanFactory,
) : ArgumentResolver {

	override fun supportsArgument(argument: Argument): Boolean {
		return resolveArgumentOrNull(argument) != null
	}

	override fun resolveArgument(argument: Argument): Any {
		return resolveArgumentOrNull(argument)!!
	}

	private fun resolveArgumentOrNull(argument: Argument): Any? {
		val qualifier = Annotations.filter(argument).getAnnotation(Qualifier::class.java)
		try {
			if(qualifier != null)
				return beanFactory.getBean(qualifier.value, argument.type.toClass())
			return beanFactory.getBean(argument.type.toClass())
		} catch(e: NoSuchBeanDefinitionException) {
			return null
		}
	}
}
