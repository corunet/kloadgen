package net.coru.kloadgen.property.editor;

import static net.coru.kloadgen.util.PropsKeysHelper.AVRO_SUBJECT_NAME;
import static net.coru.kloadgen.util.PropsKeysHelper.SCHEMA_PROPERTIES;
import static net.coru.kloadgen.util.SchemaRegistryKeyHelper.SCHEMA_REGISTRY_PASSWORD_KEY;
import static net.coru.kloadgen.util.SchemaRegistryKeyHelper.SCHEMA_REGISTRY_URL;
import static net.coru.kloadgen.util.SchemaRegistryKeyHelper.SCHEMA_REGISTRY_USERNAME_KEY;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.tomakehurst.wiremock.WireMockServer;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;
import net.coru.kloadgen.config.avroserialized.AvroSerializedConfigElement;
import net.coru.kloadgen.model.FieldValueMapping;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.apache.jmeter.util.JMeterUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.lanwen.wiremock.ext.WiremockResolver;
import ru.lanwen.wiremock.ext.WiremockResolver.Wiremock;
import ru.lanwen.wiremock.ext.WiremockUriResolver;

@ExtendWith({
    WiremockResolver.class,
    WiremockUriResolver.class
})
class AvroSubjectPropertyEditorTest {

  @BeforeEach
  public void setUp() {
    File file = new File("src/test/resources");
    String absolutePath = file.getAbsolutePath();
    JMeterUtils.loadJMeterProperties(absolutePath + "/kloadgen.properties");
    JMeterContext jmcx = JMeterContextService.getContext();
    jmcx.setVariables(new JMeterVariables());
    JMeterUtils.setLocale(Locale.ENGLISH);
  }

  @Test
  public void iterationStart(@Wiremock WireMockServer server) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    JMeterContextService.getContext().getProperties().put(SCHEMA_REGISTRY_URL, "http://localhost:" + server.port());
    JMeterContextService.getContext().getProperties().put(SCHEMA_REGISTRY_USERNAME_KEY, "foo");
    JMeterContextService.getContext().getProperties().put(SCHEMA_REGISTRY_PASSWORD_KEY, "foo");

    AvroSerializedConfigElement avroSerializedConfigElement = new AvroSerializedConfigElement("avroSubject", Collections.emptyList(), "AVRO");
    JMeterVariables variables = JMeterContextService.getContext().getVariables();
    avroSerializedConfigElement.iterationStart(null);

    assertThat(variables).isNotNull();
    assertThat(variables.getObject(AVRO_SUBJECT_NAME)).isNotNull();
    assertThat(variables.getObject(SCHEMA_PROPERTIES)).isNotNull();

  }


  private static Stream<Arguments> parametersForMergeValue() {
    return Stream.of(Arguments.of(new ArrayList<FieldValueMapping>(), new ArrayList<FieldValueMapping>(), new ArrayList<FieldValueMapping>()),
        Arguments.of(new ArrayList<>(Collections.singletonList(new FieldValueMapping("fieldName", "fieldType"))),
            new ArrayList<FieldValueMapping>(),
            new ArrayList<FieldValueMapping>()),
        Arguments.of(new ArrayList<>(Collections.singletonList(new FieldValueMapping("fieldName", "fieldType"))),
            Collections.singletonList(new FieldValueMapping("fieldSchema1", "string")),
            Collections.singletonList(new FieldValueMapping("fieldSchema1", "string"))),
        Arguments.of(new ArrayList<>(Collections.singletonList(new FieldValueMapping("fieldSchema1", "int"))),
            Collections.singletonList(new FieldValueMapping("fieldSchema1", "string")),
            Collections.singletonList(new FieldValueMapping("fieldSchema1", "string"))),
        Arguments.of(new ArrayList<>(Collections.singletonList(new FieldValueMapping("fieldSchema1", "string"))),
            Collections.singletonList(new FieldValueMapping("fieldSchema1", "string")),
            Collections.singletonList(new FieldValueMapping("fieldSchema1", "string"))),
        Arguments.of(new ArrayList<>(
                Collections.singletonList(new FieldValueMapping("fieldSchema1", "string", 0, "[\"value1\"]"))),
            Arrays.asList(new FieldValueMapping("fieldSchema1", "string"), new FieldValueMapping("field2", "string")),
            Arrays.asList(new FieldValueMapping("fieldSchema1", "string", 0, "[\"value1\"]"), new FieldValueMapping("field2", "string"))),
        Arguments.of("value",
            Collections.singletonList(new FieldValueMapping("field2", "string")),
            Collections.singletonList(new FieldValueMapping("field2", "string"))));
  }

  @ParameterizedTest
  @MethodSource("parametersForMergeValue")
  public void mergeValueTest(Object atributeListTable, List<FieldValueMapping> attributeList, List<FieldValueMapping> expected) {

    List<FieldValueMapping> result = new AvroSubjectPropertyEditor().mergeValue(atributeListTable, attributeList);

    assertThat(result).isEqualTo(expected);

  }



}