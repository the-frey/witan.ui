(defproject witan.ui "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.5.3"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.async "0.2.385" :exclusions [org.clojure/tools.reader]]
                 [org.clojure/clojurescript "1.9.36" :scope "provided"]
                 [devcards "0.2.1-6"]
                 [sablono "0.6.2"]
                 [reagent "0.6.0-rc"]
                 [cljs-log "0.2.2"]
                 [jarohen/chord "0.7.0"]
                 [com.andrewmcveigh/cljs-time "0.4.0"]
                 [bidi "2.0.0"]
                 [venantius/accountant "0.1.7"]
                 [environ "1.0.2"]
                 [cljs-ajax "0.5.3"]
                 [inflections "0.12.2"]
                 [prismatic/schema "1.1.2"]
                 [com.cognitect/transit-cljs "0.8.239"]
                 [com.outpace/schema-transit "0.2.3"]
                 [witan.gateway.schema "0.1.1"]
                 [com.cemerick/url "0.1.1"]
                 ;;
                 [cljsjs/dialog-polyfill "0.4.3-0"]
                 [cljsjs/moment "2.10.6-4"]
                 [cljsjs/mustache "1.1.0-0"]
                 [cljsjs/filesaverjs "1.1.20151003-0"]
                 [cljsjs/clipboard "1.5.9-0"]]

  :plugins [[lein-figwheel "0.5.4-3" :exclusions [org.clojure/tools.reader]]
            [lein-cljsbuild "1.1.2" :exclusions [[org.clojure/clojure]]]
            [lein-garden "0.2.6"]]

  :profiles {:uberjar {:auto-clean false}
             :dev {:dependencies [[com.cemerick/piggieback "0.2.1"]
                                  [org.clojure/tools.nrepl "0.2.12"]
                                  [ring/ring-defaults "0.1.5"]
                                  [compojure "1.4.0"]
                                  [figwheel "0.5.4-3"]
                                  [figwheel-sidecar "0.5.4-3"]]
                   :source-paths ["dev-src"]
                   :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}}

  :source-paths ["src/clj"]

  :repl-options {:init-ns user}

  :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                    "target"]

  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["src/cljs"]

                ;; If no code is to be run, set :figwheel true for continued automagical reloading
                :figwheel {:on-jsload "witan.ui.core/on-js-reload"}

                :compiler {:main witan.ui.core
                           :asset-path "/js/compiled/out"
                           :output-to "resources/public/js/compiled/witan.ui.js"
                           :output-dir "resources/public/js/compiled/out"
                           :source-map-timestamp true
                           :warnings {:single-segment-namespace false}}}
               {:id "devcards"
                :source-paths ["src/cljs"]
                :figwheel { :devcards true } ;; <- note this
                :compiler {:main       witan.ui.core
                           :asset-path "/js/compiled/devcards_out"
                           :output-to  "resources/public/js/compiled/devcards_devcards.js"
                           :output-dir "resources/public/js/compiled/devcards_out"
                           :source-map-timestamp true }}
               {:id "prod"
                :source-paths ["src/cljs"]
                :compiler {:output-to "resources/public/js/compiled/witan.ui.js"
                           :main witan.ui.core
                           :externs ["../js/externs.js"]
                           :optimizations :advanced
                           :pretty-print false
                           :warnings {:single-segment-namespace false}}}]}

  :garden {:builds [{;; Optional name of the build:
                     :id "style"
                     ;; Source paths where the stylesheet source code is
                     :source-paths ["src/styles"]
                     ;; The var containing your stylesheet:
                     :stylesheet witan.ui.style/main
                     ;; Compiler flags passed to `garden.core/css`:
                     :compiler {;; Where to save the file:
                                :output-to "resources/public/css/style.css"
                                :vendors [:moz :webkit :o :ms]
                                ;; Compress the output?
                                :pretty-print? false}}]}

  :figwheel {:css-dirs ["resources/public/css"] ;; watch and update CSS
             :nrepl-port 7888
             :ring-handler witan.ui.server/handler}
  :jvm-opts ["-Xmx2g"]
  :release-tasks [["change" "version"
                   "leiningen.release/bump-version" "release"]
                  ["vcs" "commit"]
                  ["vcs" "tag" "release-v"]
                  ["change" "version" "leiningen.release/bump-version"]
                  ["vcs" "commit"]
                  ["vcs" "push"]])
