package com.nikhil.inventory.adaptiveinventoryforecastingservice.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class BizLogs {
  public static final Logger ORDER   = LoggerFactory.getLogger("biz.order");
  public static final Logger FORECAST= LoggerFactory.getLogger("biz.forecast");
  public static final Logger AUTH    = LoggerFactory.getLogger("biz.auth");
  private BizLogs() {}
}