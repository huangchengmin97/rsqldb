/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.rsqldb.parser.parser.sqlnode;

import org.apache.rocketmq.streams.common.datatype.StringDataType;
import com.alibaba.rsqldb.parser.parser.builder.SelectSQLBuilder;
import com.alibaba.rsqldb.parser.parser.result.ConstantParseResult;
import com.alibaba.rsqldb.parser.parser.result.IParseResult;
import com.alibaba.rsqldb.parser.parser.result.ScriptParseResult;

public abstract class AbstractSelectNodeParser<T> extends AbstractSqlNodeParser<T, SelectSQLBuilder> {

    protected IParseResult createExpression(String varName, String functionName, IParseResult value) {
        String dataTypeValue = "";
        if (value instanceof ConstantParseResult) {
            ConstantParseResult constantParseResult = (ConstantParseResult)value;
            String dataTypeName = constantParseResult.getDataType().getDataTypeName();
            if (!StringDataType.getTypeName().equals(dataTypeName)) {
                dataTypeValue = "," + dataTypeName;
            }
        }
        //FIXME bug: value may be a function or field of table
        String expression = "(" + varName + "," + functionName + dataTypeValue + "," + value.getValueForSubExpression()
            + ")";
        ScriptParseResult scriptParseResult = new ScriptParseResult();
        scriptParseResult.addScript(expression);
        return scriptParseResult;
    }

    @Override
    public SelectSQLBuilder create() {
        return new SelectSQLBuilder();
    }
}
