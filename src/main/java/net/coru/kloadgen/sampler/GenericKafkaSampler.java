/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.coru.kloadgen.sampler;

import lombok.extern.slf4j.Slf4j;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;

import java.util.Properties;

import static net.coru.kloadgen.util.ProducerKeysHelper.KEY_SERIALIZER_CLASS_CONFIG_DEFAULT;
import static net.coru.kloadgen.util.ProducerKeysHelper.VALUE_SERIALIZER_CLASS_CONFIG_DEFAULT;

@Slf4j
public class GenericKafkaSampler extends AbstractKafkaSampler {

    public GenericKafkaSampler() {
        super();
    }

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = SamplerUtil.getCommonDefaultParameters();
        defaultParameters.addArgument(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KEY_SERIALIZER_CLASS_CONFIG_DEFAULT);
        defaultParameters.addArgument(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, VALUE_SERIALIZER_CLASS_CONFIG_DEFAULT);
        return defaultParameters;
    }

    @Override
    protected Properties properties(JavaSamplerContext context) {
        Properties props = SamplerUtil.setupCommonProperties(context);
        log.debug("Populated properties: {}", props);
        return props;
    }

    @Override
    protected Logger logger() {
        return log;
    }

}
