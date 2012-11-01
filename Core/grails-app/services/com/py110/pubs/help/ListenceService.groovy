package com.py110.pubs.help

class ListenceService {

    public  boolean isValid(){
        boolean  ibContinue = true
     /*   def senc
        def sListence = ApplicationHolder.application.config.listence
        def sPattern = /\b\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}\b/
        def dataSourceUrl = ApplicationHolder.application.config.dataSource.url
        def m = ( dataSourceUrl =~ sPattern)
//        def senc ="10.5.24.84zysoft"
        if (m.count > 0) {
            println "Ip:" +m[0]
            senc = m[0] +"py110"
            BasicPasswordEncryptor encryptor  = new BasicPasswordEncryptor()
            try{
                switch (Environment.current) {
                    case Environment.DEVELOPMENT:
                        ibContinue = encryptor.checkPassword(senc.toString(),sListence.toString())
                        break
                    case Environment.PRODUCTION:
                        ibContinue = encryptor.checkPassword(senc.toString(),sListence.toString())
                        break
                }
            }catch (EncryptionOperationNotPossibleException e){
                println "encryptor error,your listence is not correct"
            }
        }else{
            ibContinue = true
        }*/


        return ibContinue
    }
}
