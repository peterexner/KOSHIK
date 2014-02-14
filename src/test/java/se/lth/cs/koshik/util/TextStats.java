/**
 * Autogenerated by Avro
 * 
 * DO NOT EDIT DIRECTLY
 */
package se.lth.cs.koshik.util;  
@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class TextStats extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"TextStats\",\"namespace\":\"org.apache.avro.mapreduce\",\"fields\":[{\"name\":\"name\",\"type\":\"string\",\"default\":\"\"},{\"name\":\"count\",\"type\":\"int\",\"default\":0}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public java.lang.CharSequence name;
  @Deprecated public int count;

  /**
   * Default constructor.
   */
  public TextStats() {}

  /**
   * All-args constructor.
   */
  public TextStats(java.lang.CharSequence name, java.lang.Integer count) {
    this.name = name;
    this.count = count;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call. 
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return name;
    case 1: return count;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }
  // Used by DatumReader.  Applications should not call. 
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: name = (java.lang.CharSequence)value$; break;
    case 1: count = (java.lang.Integer)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'name' field.
   */
  public java.lang.CharSequence getName() {
    return name;
  }

  /**
   * Sets the value of the 'name' field.
   * @param value the value to set.
   */
  public void setName(java.lang.CharSequence value) {
    this.name = value;
  }

  /**
   * Gets the value of the 'count' field.
   */
  public java.lang.Integer getCount() {
    return count;
  }

  /**
   * Sets the value of the 'count' field.
   * @param value the value to set.
   */
  public void setCount(java.lang.Integer value) {
    this.count = value;
  }

  /** Creates a new TextStats RecordBuilder */
  public static se.lth.cs.koshik.util.TextStats.Builder newBuilder() {
    return new se.lth.cs.koshik.util.TextStats.Builder();
  }
  
  /** Creates a new TextStats RecordBuilder by copying an existing Builder */
  public static se.lth.cs.koshik.util.TextStats.Builder newBuilder(se.lth.cs.koshik.util.TextStats.Builder other) {
    return new se.lth.cs.koshik.util.TextStats.Builder(other);
  }
  
  /** Creates a new TextStats RecordBuilder by copying an existing TextStats instance */
  public static se.lth.cs.koshik.util.TextStats.Builder newBuilder(se.lth.cs.koshik.util.TextStats other) {
    return new se.lth.cs.koshik.util.TextStats.Builder(other);
  }
  
  /**
   * RecordBuilder for TextStats instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<TextStats>
    implements org.apache.avro.data.RecordBuilder<TextStats> {

    private java.lang.CharSequence name;
    private int count;

    /** Creates a new Builder */
    private Builder() {
      super(se.lth.cs.koshik.util.TextStats.SCHEMA$);
    }
    
    /** Creates a Builder by copying an existing Builder */
    private Builder(se.lth.cs.koshik.util.TextStats.Builder other) {
      super(other);
    }
    
    /** Creates a Builder by copying an existing TextStats instance */
    private Builder(se.lth.cs.koshik.util.TextStats other) {
            super(se.lth.cs.koshik.util.TextStats.SCHEMA$);
      if (isValidValue(fields()[0], other.name)) {
        this.name = data().deepCopy(fields()[0].schema(), other.name);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.count)) {
        this.count = data().deepCopy(fields()[1].schema(), other.count);
        fieldSetFlags()[1] = true;
      }
    }

    /** Gets the value of the 'name' field */
    public java.lang.CharSequence getName() {
      return name;
    }
    
    /** Sets the value of the 'name' field */
    public se.lth.cs.koshik.util.TextStats.Builder setName(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.name = value;
      fieldSetFlags()[0] = true;
      return this; 
    }
    
    /** Checks whether the 'name' field has been set */
    public boolean hasName() {
      return fieldSetFlags()[0];
    }
    
    /** Clears the value of the 'name' field */
    public se.lth.cs.koshik.util.TextStats.Builder clearName() {
      name = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /** Gets the value of the 'count' field */
    public java.lang.Integer getCount() {
      return count;
    }
    
    /** Sets the value of the 'count' field */
    public se.lth.cs.koshik.util.TextStats.Builder setCount(int value) {
      validate(fields()[1], value);
      this.count = value;
      fieldSetFlags()[1] = true;
      return this; 
    }
    
    /** Checks whether the 'count' field has been set */
    public boolean hasCount() {
      return fieldSetFlags()[1];
    }
    
    /** Clears the value of the 'count' field */
    public se.lth.cs.koshik.util.TextStats.Builder clearCount() {
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    public TextStats build() {
      try {
        TextStats record = new TextStats();
        record.name = fieldSetFlags()[0] ? this.name : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.count = fieldSetFlags()[1] ? this.count : (java.lang.Integer) defaultValue(fields()[1]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }
}
