(defproject clj-fire.core "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.7.1"
  
  :plugins [[deraen/lein-sass4clj "0.5.5"]
            [lein-cljfmt "0.9.0"]]
  :sass {:target-path "resources/public/css"
         :source-paths ["resources/css"]}

  :dependencies [[bidi "2.1.6"]
                 [day8.re-frame/http-fx "0.2.4"]
                 [lifecheq/pushy "0.3.9"]
                 [org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.11.4"]
                 [reagent "1.1.1"  :exclusions [cljsjs/react cljsjs/react-dom cljsjs/react-dom-server]]
                 [re-frame "1.2.0"]]

  :source-paths ["clj-src" "cljs-src"]

  :aliases {"fig:build" ["trampoline" "run" "-m" "figwheel.main" "-b" "dev" "-r"]
            "fig:min"   ["run" "-m" "figwheel.main" "-O" "advanced" "-bo" "dev"]
            "fig:test"  ["run" "-m" "figwheel.main" "-co" "test.cljs.edn" "-m" "clj-fire.test-runner"]
            "cljfmt-fix" ["cljfmt" "fix"]}

  :profiles {:dev {:dependencies [[com.bhauman/figwheel-main "0.2.17"]
                                  [org.slf4j/slf4j-nop "1.7.30"]
                                  [com.bhauman/rebel-readline-cljs "0.1.4"]
                                  [metosin/reitit "0.5.18"]]
                   
                   :resource-paths ["target"]
                   ;; need to add the compiled assets to the :clean-targets
                   :clean-targets ^{:protect false} ["target"]}})

