package com.bfsi.egalite.sync;

import java.io.IOException;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;

/**
 * Creates GSON builder with custom Date adapter that read / writes
 * java.util.Date in long data type
 * 
 * @author vijay
 * 
 */
public class GsonAdapter {

	private static GsonBuilder gsonBuilder = new GsonBuilder();

	static {
		gsonBuilder.registerTypeAdapter(Date.class, new TypeAdapter<Date>() {
			@Override
			public Date read(com.google.gson.stream.JsonReader reader)
					throws IOException {
				long time = reader.nextLong();
				return new Date(time);
			}
			@Override
			public void write(JsonWriter writer, Date date) throws IOException {
				writer.value(date.getTime());
			}
		});
	}
	/**
	 * Creates GSON that handles date format issues
	 * 
	 * @return
	 */
	public static Gson getGson() {
		return gsonBuilder.create();
	}

}
