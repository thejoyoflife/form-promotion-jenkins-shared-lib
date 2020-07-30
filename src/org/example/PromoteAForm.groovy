package org.example

import groovy.json.JsonOutput

def promote(formname) {
  def data = [forms: [
    [name: formname, organization: [id: "GA"]]
  ]]

  def basicAuth = "${params.DEV_TO_TEST_EXPORT_API_CREDENTIAL}".bytes.encodeBase64().toString()
  
  echo "Promoting: $formname, with data: ${JsonOutput.toJson(data)} and credential: $basicAuth, to the url: ${params.DEV_TO_TEST_EXPORT_API_URL}"

  def response = httpRequest(
    acceptType: 'APPLICATION_OCTETSTREAM',
    consoleLogResponseBody: true,
    contentType: 'APPLICATION_JSON',
    httpMode: 'POST',
    requestBody: JsonOutput.toJson(data),
    ignoreSslErrors: true,
    customHeaders: [[name: "Authorization", value: "Basic $basicAuth"]]
    url: "${params.DEV_TO_TEST_EXPORT_API_URL}",
    validResponseCodes: '200'
  )

  echo "Response Status: ${response.status}"
  echo "Response Content: ${response.content}" 
}