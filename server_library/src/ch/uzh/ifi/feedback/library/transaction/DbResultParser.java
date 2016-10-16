package ch.uzh.ifi.feedback.library.transaction;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Timestamp;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.uzh.ifi.feedback.library.rest.annotations.DbAttribute;
import ch.uzh.ifi.feedback.library.rest.service.ItemBase;

public abstract class DbResultParser<T> {
	
	private Map<String, Field> fields;
	
	public DbResultParser(Class<?> clazz)
	{
		InitFields(clazz);
	}
	
	private void InitFields(Class<?> clazz)
	{
		fields = new HashMap<>();
		for (Field f : ItemBase.GetFields(clazz, new ArrayList<>()))
		{
			f.setAccessible(true);
			if(f.isAnnotationPresent(DbAttribute.class))
			{
				fields.put(f.getAnnotation(DbAttribute.class).value(), f);
			}else{
				fields.put(f.getName(), f);
			}
		}
	}
	
	public void SetFields(T obj, ResultSet rs) throws SQLException
	{
		ResultSetMetaData metadata = rs.getMetaData();
		for(int i=0; i<metadata.getColumnCount(); i++)
		{
			String columnName = metadata.getColumnLabel(i + 1);
			try {
				if(fields.containsKey(columnName))
				{
					Field f = fields.get(columnName);
					if(f.getType().isEnum()){
						Method parser = f.getType().getMethod("valueOf", String.class);
						Object value = parser.invoke(null, rs.getObject(i + 1));
						fields.get(columnName).set(obj, value);
					}
					else
					{
						fields.get(columnName).set(obj, rs.getObject(i + 1));
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Map<String, Field> GetFields()
	{
		return this.fields;
	}

}
