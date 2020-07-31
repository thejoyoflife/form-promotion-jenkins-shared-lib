package smartform

def promoteFormsInFile() {
  echo "Promoting file: ${params.DEV_TO_TEST_FORM_FILENAME}"  
  def fileContent = readFile file: "${params.DEV_TO_TEST_FORM_FILENAME}"  
  fileContent.split("\n").each { line ->
    if (line?.trim().length() > 0) {
      def parts = line.trim().split(",")
      if (parts?.size() >= 2) {        
        def formName = parts[0].trim();
        def state = parts[1].trim();
        if (formName?.length() > 0 && state?.length() > 0) {
          promote(formName, state)          
        }
      }
    }
  } 
  echo "Completed promotion of file: ${params.DEV_TO_TEST_FORM_FILENAME}"
}

def promote(formname, state) {
  echo "Promoting form: ${formname} and ${state}..."
  /*
  withCredentials([usernamePassword(
                      credentialsId: "${params.DEV_TO_TEST_API_CREDENTIAL}", 
                      usernameVariable: 'username', 
                      passwordVariable: 'password')]) {
    def basicAuth = "${username}:${password}".bytes.encodeBase64().toString()                
    exportForm formname, state, basicAuth
    importForm formname, basicAuth    
  }
  deleteFormFile formname*/
  echo "Completed promotion of form: ${formname} and ${state}"
}

def exportForm(formname, state, basicAuth) {
  def data = [forms: [
    [name: formname, organization: [id: state]]
  ]]

  echo "Exporting the binary from: ${params.DEV_TO_TEST_EXPORT_API_URL}..."
  echo "With Data: ${JsonOutput.toJson(data)}..."
  echo "And Basic Credential: ${basicAuth}..."
  def eResponse = httpRequest(
    acceptType: 'APPLICATION_OCTETSTREAM',
    contentType: 'APPLICATION_JSON',
    httpMode: 'POST',
    requestBody: JsonOutput.toJson(data),
    ignoreSslErrors: true,
    customHeaders: [[name: "Authorization", value: "Basic $basicAuth"]],
    outputFile: formname + Constants.DEFAULT_SUFFIX_FILENAME_RESPONSE,
    url: "${params.DEV_TO_TEST_EXPORT_API_URL}"    
  )
  echo "Download Response Status: ${eResponse.status}" 
} 

def importForm(formname, basicAuth) {
  echo "Importing the binary to: ${params.DEV_TO_TEST_IMPORT_API_URL}..."
  echo "With File: ${formname + Constants.DEFAULT_SUFFIX_FILENAME_RESPONSE}..."
  echo "And Basic Credential: ${basicAuth}..."
  
  def iResponse = httpRequest(
    acceptType: 'TEXT_PLAIN',
    contentType: 'APPLICATION_OCTETSTREAM',
    httpMode: 'POST',
    uploadFile: formname + Constants.DEFAULT_SUFFIX_FILENAME_RESPONSE,
    multipartName: "file",
    ignoreSslErrors: true,
    customHeaders: [[name: "Authorization", value: "Basic $basicAuth"]],
    url: "${params.DEV_TO_TEST_IMPORT_API_URL}"    
  )
  echo "Upload Response Status: ${iResponse.status}"
  echo "Upload Response Content: ${iResponse.content}"
}


def deleteFormFile(formname) {
  new File("$WORKSPACE", formname + Constants.DEFAULT_SUFFIX_FILENAME_RESPONSE).delete()
}