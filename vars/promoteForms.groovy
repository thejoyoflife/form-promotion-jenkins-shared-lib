#!/usr/bin/env groovy

def call() {
  pipeline {
    agent any

    parameters {      
      string(name: "DEV_TO_TEST_EXPORT_API_URL", defaultValue: "", description: "Export API URL for dev to test environment promotion")      
      string(name: "DEV_TO_TEST_IMPORT_API_URL", defaultValue: "", description: "Import API URL for dev to test environment promotion")      
      string(name: "DEV_TO_TEST_API_CREDENTIAL", defaultValue: "SMARTFORM_PROMOTION_DEV", description: "Export/Import API Credential for dev to test environment promotion")
      string(name: "DEV_TO_TEST_FORM_FILENAME", defaultValue: "dev-to-test-form-promotion", description: "Dev to test form promotion filename")
    }

    stages {
        stage('Promote Forms In A File') {
            steps {
              script {
                def fp = new smartform.PromoteAForm()
                fp.promoteFormsInFile()
              }              
            }
        }
    }
}
}