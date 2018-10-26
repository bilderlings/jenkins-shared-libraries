import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.transform.Field

@Field
Map buildStateMap = ['SUCCESS': 'SUCCESSFUL', 'FAILURE': 'FAILED', 'UNSTABLE': 'FAILED']

@Field
List bitbucketStatuses = ['INPROGRESS', 'SUCCESSFUL', 'FAILED']

def call(String status=null) {
    def bitbucketStatusNotifyParams = [:]
    if (status == null) {
        def result = currentBuild.currentResult
        if (!buildStateMap.containsKey(result)) {
            echo "bitbucketStatusNotify is muted. Undefined build status: ${result}"
            return
        }
        bitbucketStatusNotifyParams.buildState = "${buildStateMap.get(result)}"
    } else {
        def statusUpperCase = status.trim().toUpperCase()
        if (bitbucketStatuses.contains(statusUpperCase)) {
            bitbucketStatusNotifyParams.buildState = "${statusUpperCase}"
        } else {
            error "Undefined bitbucket status: ${status}"
        }
    }
    bitbucketStatusNotifyParams.commitId = "${commitId()}"
    bitbucketStatusNotifyParams.repoSlug = "${imageName()}"

    send(bitbucketStatusNotifyParams)
}


private send(Map params){

    if ("${BUILD_ID}" == '1'){
        try {
            sendViaAPI(params)
        }catch(MissingMethodException e){
            throw e
        }catch(Exception e){
            echo "${e}"
            bitbucketStatusNotify params
        }
    }else {
        bitbucketStatusNotify params
    }
}

private sendViaAPI(Map params){
    def bitbucketApiUrl = 'https://api.bitbucket.org/2.0/repositories/bilderlings'
    if (env.BITBUCKET_API_URL){
        bitbucketApiUrl = "${env.BITBUCKET_API_URL}"
    }
    def url = "${bitbucketApiUrl}/${params.repoSlug}/commit/${params.commitId}/statuses/build"
    def blueOceanPipelineUrl = "${JENKINS_URL}blue/organizations/jenkins/${params.repoSlug}/detail/${BRANCH_NAME}/${BUILD_ID}/pipeline/"
    def data = [
            state: params.buildState,
            url: blueOceanPipelineUrl,
            key: params.repoSlug
    ]
    def body = JsonOutput.toJson(data)
    httpRequest url: url,
                authentication: 'bitbucket-oauth-credentials',
                httpMode: 'POST',
                requestBody: body,
                contentType: 'APPLICATION_JSON',
                customHeaders: [getAuthorizationHeader()],
                validResponseCodes: '200:201',
                consoleLogResponseBody: true
}

private String getAuthorizationHeader(){
    def req = httpRequest   url: 'https://bitbucket.org/site/oauth2/access_token',
                            authentication: 'bitbucket-oauth-credentials',
                            httpMode: 'POST',
                            requestBody: 'grant_type=client_credentials',
                            contentType: 'APPLICATION_FORM',
                            validResponseCodes: '200:201',
                            consoleLogResponseBody: false

    def jsonSlurper = new JsonSlurper()
    def body = jsonSlurper.parseText(req.content)
    [ name: 'Authorization', value: "Bearer ${body['access_token']}".toString()]
}
