pipeline {
    agent {
      kubernetes {
        label 'ui-test'
      }
    }
    triggers {
	cron('@midnight')
	pollSCM('H/5 * * * *')
    }
    tools {
        maven 'apache-maven-latest'
        jdk 'adoptopenjdk-hotspot-jdk8-latest'
    }
    stages {
        stage('Build') {
            steps {
                wrap([$class: 'Xvnc', takeScreenshot: false, useXauthority: true]) {
		    sh '''
                        mvn -f org.eclipse.swtchart.cbi/pom.xml clean install
                    '''
		}
            }
        }
    }
}
