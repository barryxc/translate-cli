/*
 * Copyright 2023 barry
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.baidu.translate.logger;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author yunfan
 * @since 2023/8/30
 */
public class LogUtils {

    private static final Logger LOGGER = Logger.getLogger("translate");

    private LogUtils() {
    }

    public static void e(String msg) {
        LOGGER.log(Level.WARNING, msg);
    }

    public static void e(String msg, Throwable throwable) {
        LOGGER.log(Level.WARNING, msg, throwable);
    }

    public static void info(String msg) {
        LOGGER.log(Level.INFO, msg);
    }
}
