/*
 * Copyright 2000-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.intellij.plugins.hcl.terraform.il.codeinsight

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.diagnostic.Logger
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext
import org.intellij.plugins.hcl.terraform.getPrevSiblingNonWhiteSpace
import org.intellij.plugins.hcl.terraform.il.TILLanguage
import org.intellij.plugins.hcl.terraform.il.psi.ILSelectExpression
import org.intellij.plugins.hcl.terraform.il.psi.ILVariable
import java.util.*

public class TILCompletionProvider : CompletionContributor() {
  init {
    extend(CompletionType.BASIC, METHOD_POSITION, MethodsCompletionProvider)
  }

  companion object {
    public val TERRAFORM_METHODS: TreeSet<String> = sortedSetOf("concat", "file", "format", "formatlist", "join", "element", "replace", "split", "length", "lookup", "keys", "values")
    private val METHOD_POSITION = PlatformPatterns.psiElement().withLanguage(TILLanguage)
        .withParent(javaClass<ILVariable>())
        .andNot(PlatformPatterns.psiElement().withSuperParent(2, javaClass<ILSelectExpression>()))

    private val LOG = Logger.getInstance(javaClass<TILCompletionProvider>())
  }

  private object MethodsCompletionProvider : CompletionProvider<CompletionParameters>() {

    override fun addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
      LOG.debug("TIL.MethodsCompletionProvider")
      val position = parameters.getPosition()
      LOG.debug("position = $position")
      val parent = position.getParent()
      LOG.debug("parent = $parent")
      LOG.debug("left = ${position.getPrevSibling()}")
      val leftNWS = position.getPrevSiblingNonWhiteSpace()
      LOG.debug("leftNWS = $leftNWS")
      for (keyword in TERRAFORM_METHODS) {
        result.addElement(LookupElementBuilder.create(keyword).bold())
      }
    }
  }
}
