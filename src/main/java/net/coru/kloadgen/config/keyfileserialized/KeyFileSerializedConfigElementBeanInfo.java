/*
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  * License, v. 2.0. If a copy of the MPL was not distributed with this
 *  * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package net.coru.kloadgen.config.keyfileserialized;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import net.coru.kloadgen.model.FieldValueMapping;
import net.coru.kloadgen.property.editor.FileSubjectPropertyEditor;
import net.coru.kloadgen.property.editor.SchemaConverterPropertyEditor;
import net.coru.kloadgen.property.editor.SchemaTypePropertyEditor;
import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.testbeans.gui.TableEditor;
import org.apache.jmeter.testbeans.gui.TypeEditor;

public class KeyFileSerializedConfigElementBeanInfo extends BeanInfoSupport {

  private static final String KEY_NAME = "keyName";

  private static final String KEY_SCHEMA_PROPERTIES = "keySchemaProperties";

  private static final String KEY_SCHEMA_DEFINITION = "keySchemaDefinition";

  private static final String KEY_SCHEMA_TYPE = "keySchemaType";

  public KeyFileSerializedConfigElementBeanInfo() {

    super(KeyFileSerializedConfigElement.class);

    createPropertyGroup("key_serialized_load_generator", new String[]{
            KEY_SCHEMA_TYPE, KEY_SCHEMA_PROPERTIES, KEY_SCHEMA_DEFINITION, KEY_NAME
    });

    PropertyDescriptor subjectNameProps = property(KEY_NAME);
    subjectNameProps.setPropertyEditorClass(FileSubjectPropertyEditor.class);
    subjectNameProps.setValue(NOT_UNDEFINED, Boolean.TRUE);
    subjectNameProps.setValue(DEFAULT, "");
    subjectNameProps.setValue(NOT_EXPRESSION, Boolean.FALSE);

    PropertyDescriptor schemaType = property(KEY_SCHEMA_TYPE);
    schemaType.setPropertyEditorClass(SchemaTypePropertyEditor.class);
    schemaType.setValue(NOT_UNDEFINED, Boolean.TRUE);
    schemaType.setValue(DEFAULT, "");
    schemaType.setValue(NOT_EXPRESSION, Boolean.FALSE);

    PropertyDescriptor avroSchemaProps = property(KEY_SCHEMA_DEFINITION);
    avroSchemaProps.setPropertyEditorClass(SchemaConverterPropertyEditor.class);
    avroSchemaProps.setValue(NOT_UNDEFINED, Boolean.TRUE);
    avroSchemaProps.setValue(DEFAULT, "");
    avroSchemaProps.setValue(NOT_EXPRESSION, Boolean.FALSE);

    TypeEditor tableEditor = TypeEditor.TableEditor;
    PropertyDescriptor tableProperties = property(KEY_SCHEMA_PROPERTIES, tableEditor);
    tableProperties.setValue(TableEditor.CLASSNAME, FieldValueMapping.class.getName());
    tableProperties.setValue(TableEditor.HEADERS,
        new String[]{
            "Field Name",
            "Field Type",
            "Field Length",
            "Field Values List"
        });
    tableProperties.setValue(TableEditor.OBJECT_PROPERTIES,
        new String[]{
            FieldValueMapping.FIELD_NAME,
            FieldValueMapping.FIELD_TYPE,
            FieldValueMapping.VALUE_LENGTH,
            FieldValueMapping.FIELD_VALUES_LIST
        });
    tableProperties.setValue(DEFAULT, new ArrayList<>());
    tableProperties.setValue(NOT_UNDEFINED, Boolean.TRUE);
  }
}
