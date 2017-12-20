package org.smileframework.ioc.bean.context;

import org.smileframework.ioc.bean.annotation.SmileComponent;
/**
 * Copyright (c) 2015 The Smile-Boot Project
 *
 * Licensed under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * @Package: org.smileframework.ioc.bean.context
 * @Description: 扩展上线文
 * @author: liuxin
 * @date: 2017/12/4 下午2:06
 */
@SmileComponent
public interface ExtApplicationContext {
    /**
     * 加载扩展上下文
     *
     * @param applicationContext
     */
    void mergeContext(ConfigApplicationContext applicationContext);


}
