/**
 *    Copyright 2006-2018 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.codegen.mybatis3.javamapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.*;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * modified
 * findByIds
 *
 * @author xiangwb
 */
public class SelectByIdsMethodGenerator extends
        AbstractJavaMapperMethodGenerator {

    private boolean isSimple;

    public SelectByIdsMethodGenerator(boolean isSimple) {
        super();
        this.isSimple = isSimple;
    }

    @Override
    public void addInterfaceElements(Interface interfaze) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();
        //import list
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType listType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        //import model
        importedTypes.add(listType);
        returnType.addTypeArgument(listType);
        method.setReturnType(returnType);
        method.setName(introspectedTable.getSelectByIdsStatementId());


        List<IntrospectedColumn> introspectedColumns = introspectedTable.getPrimaryKeyColumns();
        boolean annotate = introspectedColumns.size() > 1;
        if (annotate) {
            importedTypes.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param")); //$NON-NLS-1$
        }
        StringBuilder sb = new StringBuilder();
        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            FullyQualifiedJavaType type = introspectedColumn.getFullyQualifiedJavaType();
            importedTypes.add(type);
            FullyQualifiedJavaType paramType = FullyQualifiedJavaType.getNewListInstance();
            paramType.addTypeArgument(type);
            Parameter parameter = new Parameter(paramType, "ids");
            if (annotate) {
                sb.setLength(0);
                sb.append("@Param(\"");
                sb.append("ids");
                sb.append("\")");
                parameter.addAnnotation(sb.toString());
            }
            method.addParameter(parameter);
        }

        addMapperAnnotations(interfaze, method);

        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);

        if (context.getPlugins().clientSelectByPrimaryKeyMethodGenerated(
                method, interfaze, introspectedTable)) {
            addExtraImports(interfaze);
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
        }
    }

    public void addMapperAnnotations(Interface interfaze, Method method) {
    }

    public void addExtraImports(Interface interfaze) {
    }
}
