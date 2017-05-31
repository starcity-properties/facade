(defproject starcity/facade "0.1.0"
  :description "Provides HTML templates and snippets for Starcity's front-end."
  :url "https://github.com/starcity-properties/facade"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha14"]
                 [enlive "1.1.6"]
                 [hickory "0.7.0"]
                 [optimus "0.19.1"]
                 [cheshire "5.6.3"]]

  :repositories {"releases" {:url        "s3://starjars/releases"
                             :username   :env/aws_access_key
                             :passphrase :env/aws_secret_key}}

  :plugins [[s3-wagon-private "1.2.0"]])
