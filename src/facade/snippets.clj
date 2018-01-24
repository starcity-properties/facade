(ns facade.snippets
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as string]))


(html/defsnippet loading-fullscreen "templates/partials/loading-fs.html" [:section] [])


(html/defsnippet app-navbar "templates/partials/app/navbar.html" [:nav]
  [& {:keys [logout-href] :or {logout-href "/logout"}}]
  [:#logout] (html/set-attr :href logout-href))
