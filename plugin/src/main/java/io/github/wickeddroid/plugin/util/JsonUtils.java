package io.github.wickeddroid.plugin.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class JsonUtils {
  public static final Gson GSON = new GsonBuilder().create();

  private JsonUtils() {}
}
