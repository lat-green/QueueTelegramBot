package com.greentree.telegram.queue.lib

import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Component
import kotlin.reflect.jvm.javaGetter

@Component
data class ChatControllerBeanPostProcessor(
	val stateMachine: StateMachine,
) : BeanPostProcessor {

	override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
		for((method, state) in getMethodsAnnotatedWith(bean::class.java, StateController::class.java)) {
			if(state.stateName == StateController::stateName.javaGetter?.defaultValue)
				stateMachine.registerStateHandler(method.name, method)
			else
				stateMachine.registerStateHandler(state.stateName, method)
		}
		return bean
	}

	private fun <A : Annotation> getMethodsAnnotatedWith(cls: Class<*>, annotationType: Class<A>) = sequence {
		for(method in cls.declaredMethods) {
			val annotation = AnnotationUtils.getAnnotation(method, annotationType)
			if(annotation != null)
				yield(method to annotation)
		}
	}
}