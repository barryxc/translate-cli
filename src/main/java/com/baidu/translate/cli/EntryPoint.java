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

package com.baidu.translate.cli;

import com.baidu.translate.demo.TransApi;
import com.baidu.translate.logger.LogUtils;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import picocli.CommandLine;

/**
 * @author yunfan
 * @since 2023/8/18
 */
@CommandLine.Command(version = "1.0", name = "translate")
public class EntryPoint implements Callable {

    @CommandLine.Option(names = {"-d", "--domain"}, description = "domain translate", defaultValue = "it")
    public String domain;
    @CommandLine.Option(names = {"-f", "--from"}, description = "from language", defaultValue = "auto")
    public String from;
    @CommandLine.Option(names = {"-t", "--to"}, description = "to language", defaultValue = "auto")
    public String to;
    @CommandLine.Parameters(index = "0", defaultValue = "", description = "translate text")
    public String query;
    // 在平台申请的APP_ID 详见 http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
    private String APP_ID = "";
    private String SECURITY_KEY = "";

    public EntryPoint() {
        loadAppConfig();
    }

    @Override
    public Object call() throws Exception {
        if (query.isEmpty()) {
            LogUtils.e("invalid params");
            return null;
        }
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        String result = jsonParse(api.getTransResult(query, from, to, domain));
        if (!StringUtils.isEmpty(result)) {
            LogUtils.info(result);
        }
        return result;
    }

    private void loadAppConfig() {
        if (StringUtils.isEmpty(APP_ID) || StringUtils.isEmpty(SECURITY_KEY)) {
            InputStream resource = getClass().getResourceAsStream("/app.properties");
            Properties properties = new Properties();
            try {
                properties.load(resource);
                APP_ID = properties.getProperty("APP_ID");
                SECURITY_KEY = properties.getProperty("SECURITY_KEY");
                if (resource != null) {
                    resource.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String jsonParse(String json) {
        JSONObject jsonObject = new JSONObject(json);
        boolean failed = jsonObject.has("error_code");
        if (failed) {
            Object errorMsg = jsonObject.get("error_msg");
            LogUtils.e(errorMsg.toString());
            return null;
        }
        boolean hasResult = jsonObject.has("trans_result");
        if (!hasResult) {
            LogUtils.e(jsonObject.toString());
            return null;
        }
        JSONArray transResult = jsonObject.getJSONArray("trans_result");
        if (transResult != null && !transResult.isEmpty()) {
            JSONObject result = transResult.getJSONObject(0);
            if (result != null) {
                Object dst = result.get("dst");
                if (dst instanceof String) {
                    return unicode2String((String) dst);
                }
            }
        }
        return null;
    }

    private String unicode2String(String unicode) {
        if (StringUtils.isBlank(unicode)) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;

        while ((i = unicode.indexOf("\\u", pos)) != -1) {
            sb.append(unicode.substring(pos, i));
            if (i + 5 < unicode.length()) {
                pos = i + 6;
                sb.append((char) Integer.parseInt(unicode.substring(i + 2, i + 6), 16));
            }
        }
        //如果pos位置后，有非中文字符，直接添加
        sb.append(unicode.substring(pos));

        return sb.toString();
    }

}
