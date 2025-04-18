/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2015, Boundless
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.mongodb.geojson;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import java.util.Date;
import org.bson.types.ObjectId;
import org.geotools.data.mongodb.MongoDataStore;
import org.geotools.data.mongodb.MongoTestSetup;

public class GeoJSONMongoTestSetup extends MongoTestSetup {

    static Date[] dateValues = {
        parseDate("2015-01-01T00:00:00.000Z"),
        parseDate("2015-01-01T16:30:00.000Z"),
        parseDate("2015-01-01T21:30:00.000Z")
    };
    static final String NULLABLE_ATTRIBUTE = "nullableAttribute";

    @Override
    protected void setUpDataStore(MongoDataStore dataStore) {}

    @Override
    public void setUp(DB db) {
        DBCollection ft1 = db.getCollection("ft1");
        ft1.drop();

        ft1.save(BasicDBObjectBuilder.start()
                .add("_id", new ObjectId("58e5889ce4b02461ad5af080"))
                .add("id", "ft1.0")
                .push("geometry")
                .add("type", "Point")
                .add("coordinates", list(0, 0))
                .pop()
                .push("properties")
                .add("intProperty", 0)
                .add("doubleProperty", 0.0)
                .add("stringProperty", "zero")
                .add("optionalProperty3", "optional")
                .add(NULLABLE_ATTRIBUTE, "A value")
                .add("listProperty", list(new BasicDBObject("value", 0.1), new BasicDBObject("value", 0.2)))
                .add("dateProperty", getDateProperty(0))
                .add("stringProperty2", "a")
                .pop()
                .get());
        ft1.save(BasicDBObjectBuilder.start()
                .add("_id", new ObjectId("58e5889ce4b02461ad5af081"))
                .add("id", "ft1.1")
                .push("geometry")
                .add("type", "Point")
                .add("coordinates", list(1, 1))
                .pop()
                .push("properties")
                .add("intProperty", 1)
                .add("doubleProperty", 1.1)
                .add("optionalProperty2", "optional")
                .add("stringProperty", "one")
                .add(NULLABLE_ATTRIBUTE, null)
                .add("listProperty", list(new BasicDBObject("value", 1.1), new BasicDBObject("value", 1.2)))
                .add("dateProperty", getDateProperty(1))
                .add("stringProperty2", "b")
                .pop()
                .get());
        ft1.save(BasicDBObjectBuilder.start()
                .add("_id", new ObjectId("58e5889ce4b02461ad5af082"))
                .add("id", "ft1.2")
                .push("geometry")
                .add("type", "Point")
                .add("coordinates", list(2, 2))
                .pop()
                .push("properties")
                .add("intProperty", 2)
                .add("doubleProperty", 2.2)
                .add("stringProperty", "two")
                .add("optionalProperty", "optional")
                .add(
                        "listProperty",
                        list(
                                new BasicDBObject("value", 2.1),
                                new BasicDBObject("value", 2.2),
                                new BasicDBObject("insideArrayValue", 7.7)))
                .add("dateProperty", getDateProperty(2))
                .add("stringProperty2", "b")
                .pop()
                .get());

        ft1.createIndex(new BasicDBObject("geometry", "2dsphere"));
        ft1.createIndex(new BasicDBObject("properties.listProperty.value", 1));

        DBCollection ft2 = db.getCollection("ft2");
        ft2.drop();

        DBCollection ft3 = db.getCollection("ft3");
        ft3.drop();

        ft3.save(BasicDBObjectBuilder.start()
                .add("_id", new ObjectId("58e5889ce4b02461ad5af090"))
                .add("id", "ft3.0")
                .push("geometry")
                .add("type", "Point")
                .add("coordinates", list(0, 1))
                .pop()
                .push("properties")
                .add("intProperty", 0)
                .add("doubleProperty", 0.0)
                .add("stringProperty", "zero")
                .add("optionalProperty3", "optional")
                .add(NULLABLE_ATTRIBUTE, "A value")
                .add("listProperty", list(new BasicDBObject("value", 0.1), new BasicDBObject("value", 0.2)))
                .add("dateProperty", getDateProperty(0))
                .add("stringProperty2", "a")
                .pop()
                .get());
        ft3.save(BasicDBObjectBuilder.start()
                .add("_id", new ObjectId("58e5889ce4b02461ad5af091"))
                .add("id", "ft3.1")
                .push("geometry")
                .add("type", "Point")
                .add("coordinates", list(1, 2))
                .pop()
                .push("properties")
                .add("intProperty", 1)
                .add("doubleProperty", 1.1)
                .add("optionalProperty2", "optional")
                .add("stringProperty", "one")
                .add(NULLABLE_ATTRIBUTE, null)
                .add("listProperty", list(new BasicDBObject("value", 1.1), new BasicDBObject("value", 1.2)))
                .add("dateProperty", getDateProperty(1))
                .add("stringProperty2", "b")
                .pop()
                .get());
        ft3.save(BasicDBObjectBuilder.start()
                .add("_id", new ObjectId("58e5889ce4b02461ad5af029"))
                .add("id", "ft3.2")
                .push("geometry")
                .add("type", "Point")
                .add("coordinates", list(2, 3))
                .pop()
                .push("properties")
                .add("intProperty", 2)
                .add("doubleProperty", 2.2)
                .add("stringProperty", "two")
                .add("optionalProperty", "optional")
                .add(
                        "listProperty",
                        list(
                                new BasicDBObject("value", 2.1),
                                new BasicDBObject("value", 2.2),
                                new BasicDBObject("insideArrayValue", 7.7)))
                .add("dateProperty", getDateProperty(2))
                .add("stringProperty2", "b")
                .pop()
                .get());

        ft3.createIndex(new BasicDBObject("geometry", "2dsphere"));
        ft3.createIndex(new BasicDBObject("properties.listProperty.value", 1));
    }

    @Override
    protected Date getDateProperty(int featureIdx) {
        if (featureIdx < dateValues.length) {
            return dateValues[featureIdx];
        }

        return null;
    }
}
