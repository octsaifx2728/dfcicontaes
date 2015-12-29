/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contadfciacop1;

/**
 *
 * @author sfx
 */
public class Main2 {
    
    
    public static void main(String args[]){
        
        String s = "SELECT   RF.RDB$FIELD_NAME FIELD_NAME,CASE F.RDB$FIELD_TYPE WHEN 7 THEN CASE F.RDB$FIELD_SUB_TYPE WHEN 0 THEN 'SMALLINT' WHEN 1 THEN 'NUMERIC(' || F.RDB$FIELD_PRECISION || ', ' || (-F.RDB$FIELD_SCALE) || ')' WHEN 2 THEN 'DECIMAL' END WHEN 8 THEN\n" +
"    CASE F.RDB$FIELD_SUB_TYPE\n" +
"        WHEN 0 THEN 'INTEGER'\n" +
"        WHEN 1 THEN 'NUMERIC('  || F.RDB$FIELD_PRECISION || ', ' || (-F.RDB$FIELD_SCALE) || ')'\n" +
"        WHEN 2 THEN 'DECIMAL'\n" +
"      END\n" +
"    WHEN 9 THEN 'QUAD'\n" +
"    WHEN 10 THEN 'FLOAT'\n" +
"    WHEN 12 THEN 'DATE'\n" +
"    WHEN 13 THEN 'TIME'\n" +
"    WHEN 14 THEN 'CHAR(' || (TRUNC(F.RDB$FIELD_LENGTH / CH.RDB$BYTES_PER_CHARACTER)) || ') '\n" +
"    WHEN 16 THEN\n" +
"      CASE F.RDB$FIELD_SUB_TYPE\n" +
"        WHEN 0 THEN 'BIGINT'\n" +
"        WHEN 1 THEN 'NUMERIC(' || F.RDB$FIELD_PRECISION || ', ' || (-F.RDB$FIELD_SCALE) || ')'\n" +
"        WHEN 2 THEN 'DECIMAL'\n" +
"      END\n" +
"    WHEN 27 THEN 'DOUBLE'\n" +
"    WHEN 35 THEN 'TIMESTAMP'\n" +
"    WHEN 37 THEN 'VARCHAR(' || (TRUNC(F.RDB$FIELD_LENGTH / CH.RDB$BYTES_PER_CHARACTER)) || ')'\n" +
"    WHEN 40 THEN 'CSTRING' || (TRUNC(F.RDB$FIELD_LENGTH / CH.RDB$BYTES_PER_CHARACTER)) || ')'\n" +
"    WHEN 45 THEN 'BLOB_ID'\n" +
"    WHEN 261 THEN 'BLOB SUB_TYPE ' || F.RDB$FIELD_SUB_TYPE\n" +
"    ELSE 'RDB$FIELD_TYPE: ' || F.RDB$FIELD_TYPE || '?'\n" +
"  END FIELD_TYPE,\n" +
"  IIF(COALESCE(RF.RDB$NULL_FLAG, 0) = 0, NULL, 'NOT NULL') FIELD_NULL,\n" +
"  CH.RDB$CHARACTER_SET_NAME FIELD_CHARSET,\n" +
"  DCO.RDB$COLLATION_NAME FIELD_COLLATION,\n" +
"  COALESCE(RF.RDB$DEFAULT_SOURCE, F.RDB$DEFAULT_SOURCE) FIELD_DEFAULT,\n" +
"  F.RDB$VALIDATION_SOURCE FIELD_CHECK,\n" +
"  RF.RDB$DESCRIPTION FIELD_DESCRIPTION FROM RDB$RELATION_FIELDS RF JOIN RDB$FIELDS F ON (F.RDB$FIELD_NAME = RF.RDB$FIELD_SOURCE) LEFT OUTER JOIN RDB$CHARACTER_SETS CH ON (CH.RDB$CHARACTER_SET_ID = F.RDB$CHARACTER_SET_ID) LEFT OUTER JOIN RDB$COLLATIONS DCO ON ((DCO.RDB$COLLATION_ID = F.RDB$COLLATION_ID) AND (DCO.RDB$CHARACTER_SET_ID = F.RDB$CHARACTER_SET_ID)) WHERE (RF.RDB$RELATION_NAME = 'ALUMNO') AND (COALESCE(RF.RDB$SYSTEM_FLAG, 0) = 0) ORDER BY RF.RDB$FIELD_POSITION; \n" +
"    \n" +
"    \n" +
"    \n" +
"    ";
        
     System.out.println(s);
        
    }
    
    
}
