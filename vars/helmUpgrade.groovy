
def call(String namespaceParameter, Map args=[:]){
    call namespace: namespaceParameter, set: args
}

def call(Map params){

    def namespace = null
    def valuesPath = null
    def args = [:]

    if (params != null){
        namespace = params.namespace?.trim()
        args = params.set
        valuesPath = params.valuesPath?.trim()
    }

    if (!namespace){
        error "Undefined namespace: ${params.namespace}"
    }
    def exposedArgs = ' '
    if (args && args.size() > 0){
        exposedArgs = ''
        args.each { k, v ->
            if (v instanceof CharSequence){
                exposedArgs += ' --set-string '
            } else {
                exposedArgs += ' --set '
            }
            exposedArgs += "\"$k=$v\""
        }
        exposedArgs += ' '
    }

    def values = ' '
    if (valuesPath == null){
        values = """ -f "chart/values-${namespace}.yaml" """
    }else if(valuesPath != ''){
        values = """ -f "${valuesPath}" """
    }

    def releaseName = "${imageName()}-${namespace}"
    sh "helm upgrade${values}--install --force --wait --namespace \"${namespace}\"${exposedArgs}\"${releaseName}\" chart/"
}
