/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
/**
 * Representation a {@linkplain org.geotools.api.feature.Feature features} on a map. A {{Feature}} may represent a
 * single item or a {{FeatureCollection}} of many things. Features with common properties may be categorised into a
 * {{FeatureType}}, forming a dynamic type system similar to those introduced into other aspects of computer science.
 *
 * <p>The contents of this package form a feature model in accordance with the ideas expressed in the ISO 19107, as
 * captured in the OGC Reference Model, GML3 and related specifications.
 *
 * <ul>
 *   <li><a href="https://www.iso.org/standard/66175.html">ISO 19107</a>
 *   <li><a href="https://www.ogc.org/standard/gml/">GML</a>
 * </ul>
 *
 * <H2>Type Model</H2>
 *
 * As mentioned above Features can make use of a FeatureType in order to describe the set of attributes, associations
 * and operations they are expected to have.
 *
 * <p>A FeatureType represents this information in PropertyDescriptors as follows:
 *
 * <ul>
 *   <li>AttributeDescriptor - defines the name of the attribute and its AttributeType
 *   <li>AssociationDescriptor - defines the name of the association and its AssociationType
 *   <li>OperationDescriptor - defines the name of the operation and its OperationType
 * </ul>
 *
 * This information is often considered "metadata" in the same manner as a Class is the metadata describing a Java
 * Object.
 *
 * <h4>Naming</h4>
 *
 * One of the subjects that comes up when talking about metadata is "what the names mean", the names recorded by the
 * PropertyDescriptors may be of some significance to the problem or domain being represented.
 *
 * <p>You can represent this significance by making a "dictionary" of your names, in rare cases you can find a formal
 * dictionary defined by an organisation. The idea of group names into a dictionary is represented by the concept of a
 * "Namespace".
 *
 * <p>A "Namespace" used to organise the names of your FeatureTypes is called a "Schema".
 *
 * <p>Please note that "Schema" and "Namespace" above are strictly referring to spatial data. This gets especially
 * confusing when working with spatial data in a web application - since the w3c XML technologies use similar language
 * to describe their ideas.
 *
 * <H2>Data Model</H2>
 *
 * The data model is the subject of this package, and centred around the idea of a Feature, it forms a part of a larger
 * picture and constructs defined here will be referenced from several other packages.
 *
 * <h4>Feature</h4>
 *
 * A Feature represents something that can be drawn onto a map. A feature acts as a model of a real world entity. As
 * such it has many similarities to object oriented programming ideas of Class, Object, Field and Method.
 *
 * <ul>
 *   <li>Feature - runtime data structure (similar in use to Object)
 *   <li>Attributes - feature property that holds information about a Feature (similar to a Field)
 *   <li>Associations - feature property defining a relationships a Feature is involved in (similar to a reference or
 *       collection)
 *   <li>Operations - feature property describing functionality a feature is able to perform (similar to a Method)
 * </ul>
 *
 * The most useful aspect of a Feature is the fact that it is a dynamic data construct defined at runtime.
 *
 * <p>Traditionally the Java programming language represents dynamic data structures using java.util.Map, you could
 * think of a Feature as a java.util.Map in which the keys (ie attribute descriptors) are well defined.
 *
 * <p>If two Features have the same set of keys they are considered to be of the same FeatureType. A FeatureType is
 * simply a list of valid attribute descriptors. You have the same kind of ability to represent your model with
 * FeatureType as your do with Java class, inheritance is supported (ie superType) as is aggregation (your attributes
 * can be Features).
 *
 * <h4>Record</h4>
 *
 * A Record is a simple data structure (it really is exactly the same as a Map with well known keys). Once again two
 * Records with the same set of keys are considered to belong to the same RecordType.
 *
 * <p>Records are most often used as the result of an Operation. For the a Coverage (which is a kind of Feature) a
 * operation is defined to "sample" a specific location, the information returned as part of this sample is a Record.
 *
 * <h4>SimpleFeature</h4>
 *
 * The idea of a simple feature is a concept introduced within the context of the GeoAPI project, many of the Java
 * projects we work with are interested in a more approachable model similar in spirit to an array of ordered values, or
 * at most a java.util.Map of named values.
 *
 * <p>A Simple feature is a Feature with the following additional requirements:
 *
 * <ul>
 *   <li>Attributes always must occur in the same order defined in their FeatureType
 *   <li>Ideas such as inheritance and aggregation are left out of it
 *   <li>Duplicate values are not allowed
 * </ul>
 *
 * These restrictions enable us to produce an API that is far easier to work with, values can be accessed by name, or by
 * order and so on.
 *
 * <p>There is evidence that this need is present in other contexts, GML 3 has been simplified to a "Simple Feature"
 * profile operating with similar restrictions to the ones indicated above.
 *
 * <H2>Query Model</H2>
 *
 * <H4>Filter</H4>
 *
 * <P ALIGN="justify">Our {{@linkplain org.geotools.api.filter.Filter filter}} constructs are used to partition values
 * into sets. We have deliberately defined Filter to work on more than just Feature instances, allowing us to use these
 * ideas to capture restrictions on individual attribute values.
 *
 * <p>Filter should be viewed as our query or constraint system.
 *
 * <H4>Identity</H4>
 *
 * <p>Closely related to the concept of constraints is one of identity, it is difficult to think of identification as a
 * topic apart from the definition of a feature as a representation of a real world entity. In actual fact identity is
 * related to the the definition of a Filter, a name is just another way of separating out a partition of values, the
 * set in this case happens to be a set of one.
 *
 * <P ALIGN="justify">The concept of Identity is a troublesome one, our model supports the concept of "feature id". In
 * the General OGC reference model the identification of a feature is intended to be unqiue and specific to the real
 * world entity being modeled (regardless of the particular set of attribute you are using to describe in the current
 * context).
 *
 * <p>As an example you may wish to model a roadway with pavement type and history of repair when looking at a budget
 * for the coming year, the same road may be modelled in the future with a collection of possible routes when planning
 * for over a ten year period. The "road" should be the same in both situations, and have the same id.
 *
 * <p>It is very difficult to be diligent with the concept of ID, GML3 for example only restricts an ID to be unique
 * within the scope of a single document. I would suggestion you chose a stable source of identification, in the example
 * above it would facilitate "joining" both datasets to predict repair costs for each of the proposed routes.
 */
package org.geotools.api.feature;
