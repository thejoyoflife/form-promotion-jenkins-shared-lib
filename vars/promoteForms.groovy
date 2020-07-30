#!/usr/bin/env groovy

import org.example.Constants
import org.example.PromoteAForm

def call(String filename = Constants.PROMOTION_FILES[0]) {
  echo "Promoting $WORKSPACE/$filename"
  def lines = new File("$WORKSPACE", filename).readLines()
  def fp = new PromoteAForm()
  lines.each {
    fp.promote(it)
  }
}