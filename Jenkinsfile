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
						mvn -f org.eclipse.swtchart.cbi/pom.xml -Peclipse-sign clean install
					'''
				}
			}
		}
		stage('deploy') {
			steps {
				sshagent ( ['projects-storage.eclipse.org-bot-ssh']) {
					sh '''
						ssh genie.swtchart@projects-storage.eclipse.org mkdir -p /home/data/httpd/download.eclipse.org/swtchart/integration/${BRANCH_NAME}/repository
						scp -r org.eclipse.swtchart.updatesite/target/repository/* genie.swtchart@projects-storage.eclipse.org:/home/data/httpd/download.eclipse.org/swtchart/integration/${BRANCH_NAME}/repository
						ssh genie.swtchart@projects-storage.eclipse.org rm -rf /home/data/httpd/download.eclipse.org/swtchart/0.7.x
						ssh genie.swtchart@projects-storage.eclipse.org rm -rf /home/data/httpd/download.eclipse.org/swtchart/0.8.0
						ssh genie.swtchart@projects-storage.eclipse.org rm -rf /home/data/httpd/download.eclipse.org/swtchart/0.8.1
						ssh genie.swtchart@projects-storage.eclipse.org rm -rf /home/data/httpd/download.eclipse.org/swtchart/0.8.x
						ssh genie.swtchart@projects-storage.eclipse.org rm -rf /home/data/httpd/download.eclipse.org/swtchart/REL-0.12.0
						ssh genie.swtchart@projects-storage.eclipse.org rm -rf /home/data/httpd/download.eclipse.org/swtchart/develop
					'''
				}
			}
		}
	}
}
