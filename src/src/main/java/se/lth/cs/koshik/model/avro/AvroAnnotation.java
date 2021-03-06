/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package se.lth.cs.koshik.model.avro;  
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class AvroAnnotation extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"AvroAnnotation\",\"namespace\":\"se.lth.cs.koshik.model.avro\",\"fields\":[{\"name\":\"layer\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"begin\",\"type\":\"int\",\"default\":-1},{\"name\":\"end\",\"type\":\"int\",\"default\":-1},{\"name\":\"content\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"features\",\"type\":{\"type\":\"map\",\"values\":\"string\"}}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public java.lang.CharSequence layer;
  @Deprecated public int begin;
  @Deprecated public int end;
  @Deprecated public java.lang.CharSequence content;
  @Deprecated public java.util.Map<java.lang.CharSequence,java.lang.CharSequence> features;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>. 
   */
  public AvroAnnotation() {}

  /**
   * All-args constructor.
   */
  public AvroAnnotation(java.lang.CharSequence layer, java.lang.Integer begin, java.lang.Integer end, java.lang.CharSequence content, java.util.Map<java.lang.CharSequence,java.lang.CharSequence> features) {
    this.layer = layer;
    this.begin = begin;
    this.end = end;
    this.content = content;
    this.features = features;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return layer;
    case 1: return begin;
    case 2: return end;
    case 3: return content;
    case 4: return features;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: layer = (java.lang.CharSequence)value$; break;
    case 1: begin = (java.lang.Integer)value$; break;
    case 2: end = (java.lang.Integer)value$; break;
    case 3: content = (java.lang.CharSequence)value$; break;
    case 4: features = (java.util.Map<java.lang.CharSequence,java.lang.CharSequence>)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'layer' field.
   */
  public java.lang.CharSequence getLayer() {
    return layer;
  }

  /**
   * Sets the value of the 'layer' field.
   * @param value the value to set.
   */
  public void setLayer(java.lang.CharSequence value) {
    this.layer = value;
  }

  /**
   * Gets the value of the 'begin' field.
   */
  public java.lang.Integer getBegin() {
    return begin;
  }

  /**
   * Sets the value of the 'begin' field.
   * @param value the value to set.
   */
  public void setBegin(java.lang.Integer value) {
    this.begin = value;
  }

  /**
   * Gets the value of the 'end' field.
   */
  public java.lang.Integer getEnd() {
    return end;
  }

  /**
   * Sets the value of the 'end' field.
   * @param value the value to set.
   */
  public void setEnd(java.lang.Integer value) {
    this.end = value;
  }

  /**
   * Gets the value of the 'content' field.
   */
  public java.lang.CharSequence getContent() {
    return content;
  }

  /**
   * Sets the value of the 'content' field.
   * @param value the value to set.
   */
  public void setContent(java.lang.CharSequence value) {
    this.content = value;
  }

  /**
   * Gets the value of the 'features' field.
   */
  public java.util.Map<java.lang.CharSequence,java.lang.CharSequence> getFeatures() {
    return features;
  }

  /**
   * Sets the value of the 'features' field.
   * @param value the value to set.
   */
  public void setFeatures(java.util.Map<java.lang.CharSequence,java.lang.CharSequence> value) {
    this.features = value;
  }

  /** Creates a new AvroAnnotation RecordBuilder */
  public static se.lth.cs.koshik.model.avro.AvroAnnotation.Builder newBuilder() {
    return new se.lth.cs.koshik.model.avro.AvroAnnotation.Builder();
  }
  
  /** Creates a new AvroAnnotation RecordBuilder by copying an existing Builder */
  public static se.lth.cs.koshik.model.avro.AvroAnnotation.Builder newBuilder(se.lth.cs.koshik.model.avro.AvroAnnotation.Builder other) {
    return new se.lth.cs.koshik.model.avro.AvroAnnotation.Builder(other);
  }
  
  /** Creates a new AvroAnnotation RecordBuilder by copying an existing AvroAnnotation instance */
  public static se.lth.cs.koshik.model.avro.AvroAnnotation.Builder newBuilder(se.lth.cs.koshik.model.avro.AvroAnnotation other) {
    return new se.lth.cs.koshik.model.avro.AvroAnnotation.Builder(other);
  }
  
  /**
   * RecordBuilder for AvroAnnotation instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<AvroAnnotation>
    implements org.apache.avro.data.RecordBuilder<AvroAnnotation> {

    private java.lang.CharSequence layer;
    private int begin;
    private int end;
    private java.lang.CharSequence content;
    private java.util.Map<java.lang.CharSequence,java.lang.CharSequence> features;

    /** Creates a new Builder */
    private Builder() {
      super(se.lth.cs.koshik.model.avro.AvroAnnotation.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(se.lth.cs.koshik.model.avro.AvroAnnotation.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.layer)) {
        this.layer = data().deepCopy(fields()[0].schema(), other.layer);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.begin)) {
        this.begin = data().deepCopy(fields()[1].schema(), other.begin);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.end)) {
        this.end = data().deepCopy(fields()[2].schema(), other.end);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.content)) {
        this.content = data().deepCopy(fields()[3].schema(), other.content);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.features)) {
        this.features = data().deepCopy(fields()[4].schema(), other.features);
        fieldSetFlags()[4] = true;
      }
    }
    
    /** Creates a Builder by copying an existing AvroAnnotation instance */
    private Builder(se.lth.cs.koshik.model.avro.AvroAnnotation other) {
            super(se.lth.cs.koshik.model.avro.AvroAnnotation.SCHEMA$);
      if (isValidValue(fields()[0], other.layer)) {
        this.layer = data().deepCopy(fields()[0].schema(), other.layer);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.begin)) {
        this.begin = data().deepCopy(fields()[1].schema(), other.begin);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.end)) {
        this.end = data().deepCopy(fields()[2].schema(), other.end);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.content)) {
        this.content = data().deepCopy(fields()[3].schema(), other.content);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.features)) {
        this.features = data().deepCopy(fields()[4].schema(), other.features);
        fieldSetFlags()[4] = true;
      }
    }

    /** Gets the value of the 'layer' field */
    public java.lang.CharSequence getLayer() {
      return layer;
    }
    
    /** Sets the value of the 'layer' field */
    public se.lth.cs.koshik.model.avro.AvroAnnotation.Builder setLayer(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.layer = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'layer' field has been set */
    public boolean hasLayer() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'layer' field */
    public se.lth.cs.koshik.model.avro.AvroAnnotation.Builder clearLayer() {
      layer = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'begin' field */
    public java.lang.Integer getBegin() {
      return begin;
    }
    
    /** Sets the value of the 'begin' field */
    public se.lth.cs.koshik.model.avro.AvroAnnotation.Builder setBegin(int value) {
      validate(fields()[1], value);
      this.begin = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'begin' field has been set */
    public boolean hasBegin() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'begin' field */
    public se.lth.cs.koshik.model.avro.AvroAnnotation.Builder clearBegin() {
      fieldSetFlags()[1] = false;
      return this;
    }

    /** Gets the value of the 'end' field */
    public java.lang.Integer getEnd() {
      return end;
    }
    
    /** Sets the value of the 'end' field */
    public se.lth.cs.koshik.model.avro.AvroAnnotation.Builder setEnd(int value) {
      validate(fields()[2], value);
      this.end = value;
      fieldSetFlags()[2] = true;
      return this; 
    }
    
    /** Checks whether the 'end' field has been set */
    public boolean hasEnd() {
      return fieldSetFlags()[2];
    }
    
    /** Clears the value of the 'end' field */
    public se.lth.cs.koshik.model.avro.AvroAnnotation.Builder clearEnd() {
      fieldSetFlags()[2] = false;
      return this;
    }

    /** Gets the value of the 'content' field */
    public java.lang.CharSequence getContent() {
      return content;
    }
    
    /** Sets the value of the 'content' field */
    public se.lth.cs.koshik.model.avro.AvroAnnotation.Builder setContent(java.lang.CharSequence value) {
      validate(fields()[3], value);
      this.content = value;
      fieldSetFlags()[3] = true;
      return this; 
    }
    
    /** Checks whether the 'content' field has been set */
    public boolean hasContent() {
      return fieldSetFlags()[3];
    }
    
    /** Clears the value of the 'content' field */
    public se.lth.cs.koshik.model.avro.AvroAnnotation.Builder clearContent() {
      content = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /** Gets the value of the 'features' field */
    public java.util.Map<java.lang.CharSequence,java.lang.CharSequence> getFeatures() {
      return features;
    }
    
    /** Sets the value of the 'features' field */
    public se.lth.cs.koshik.model.avro.AvroAnnotation.Builder setFeatures(java.util.Map<java.lang.CharSequence,java.lang.CharSequence> value) {
      validate(fields()[4], value);
      this.features = value;
      fieldSetFlags()[4] = true;
      return this; 
    }
    
    /** Checks whether the 'features' field has been set */
    public boolean hasFeatures() {
      return fieldSetFlags()[4];
    }
    
    /** Clears the value of the 'features' field */
    public se.lth.cs.koshik.model.avro.AvroAnnotation.Builder clearFeatures() {
      features = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    @Override
    public AvroAnnotation build() {
      try {
        AvroAnnotation record = new AvroAnnotation();
        record.layer = fieldSetFlags()[0] ? this.layer : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.begin = fieldSetFlags()[1] ? this.begin : (java.lang.Integer) defaultValue(fields()[1]);
        record.end = fieldSetFlags()[2] ? this.end : (java.lang.Integer) defaultValue(fields()[2]);
        record.content = fieldSetFlags()[3] ? this.content : (java.lang.CharSequence) defaultValue(fields()[3]);
        record.features = fieldSetFlags()[4] ? this.features : (java.util.Map<java.lang.CharSequence,java.lang.CharSequence>) defaultValue(fields()[4]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
