(ns facade.core
  (:require [clojure.string :as string]
            [facade.html :refer :all]
            [facade.optimus :as foptimus]
            [facade.snippets :as snippets]
            [hickory.core :as h]
            [net.cgrand.enlive-html :as html :refer [deftemplate]]
            [optimus.link :as link]))

;; =============================================================================
;; Helpers
;; =============================================================================


(defmacro maybe-substitute
  ([expr] `(if-let [x# ~expr] (html/substitute x#) identity))
  ([expr & exprs] `(maybe-substitute (or ~expr ~@exprs))))


(defmacro maybe-prepend
  ([expr] `(if-let [x# ~expr] (html/prepend x#) identity))
  ([expr & exprs] `(maybe-prepend (or ~expr ~@exprs))))


(defmacro maybe-append
  ([expr] `(if-let [x# ~expr] (html/append x#) identity))
  ([expr & exprs] `(maybe-append (or ~expr ~@exprs))))


(defmacro maybe-content
  ([expr] `(if-let [x# ~expr] (html/content x#) identity))
  ([expr & exprs] `(maybe-content (or ~expr ~@exprs))))


(defn maybe-errors
  "If `errors` are non-nil, append them to the selected container; otherwise,
  hide the selected container."
  [errors]
  (if errors
    (->> errors
         (map (fn [e] [:div.alert.alert-error.mb2 e]))
         html/html
         html/append)
    (html/add-class "dn")))


(defn maybe-messages
  "If `errors` are non-nil, append them to the selected container; otherwise,
  hide the selected container."
  [msgs]
  (if msgs
    (->> msgs
         (map (fn [m] [:div.alert.alert-success.mb2 m]))
         html/html
         html/append)
    (html/add-class "dn")))


;; =============================================================================
;; Components
;; =============================================================================


;; See https://github.com/cgrand/enlive/issues/110
(defn hickory-parser
  "Loads and parse an HTML resource and closes the stream."
  [stream]
  (filter map? (map h/as-hickory (h/parse-fragment (slurp stream)))))


(def default-fonts
  "https://fonts.googleapis.com/css?family=Caveat|Eczar:800|Work+Sans:400,600")


(def lato-fonts
  "https://fonts.googleapis.com/css?family=Lato:300,400,700,900")


(def font-awesome
  "https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css")


(def chatlio
  (html/html
   [:script "window._chatlio = window._chatlio||[];
     !function(){ var t=document.getElementById('chatlio-widget-embed');if(t&&window.ChatlioReact&&_chatlio.init)return void _chatlio.init(t,ChatlioReact);for(var e=function(t){return function(){_chatlio.push([t].concat(arguments)) }},i=['configure','identify','track','show','hide','isShown','isOnline'],a=0;a<i.length;a++)_chatlio[i[a]]||(_chatlio[i[a]]=e(i[a]));var n=document.createElement('script'),c=document.getElementsByTagName('script')[0];n.id='chatlio-widget-embed',n.src='https://w.chatlio.com/w.chatlio-widget.js',n.async=!0,n.setAttribute('data-embed-version','2.1');
       n.setAttribute('data-widget-id','245bd50d-a161-4f3b-58fd-3168c7006512');
       n.setAttribute('data-start-hidden', true)
       c.parentNode.insertBefore(n,c);
     }();"]))


;; =============================================================================
;; Templates
;; =============================================================================


(deftemplate app "templates/app.html"
  [req app-name & {:keys [stylesheets navbar json scripts asset-path
                          content fonts chatlio? css-bundles title]
                   :or   {fonts      [default-fonts]
                          asset-path "/assets/img/"}}]
  [:head :title] (html/content (or title (str "Starcity - " (string/capitalize app-name))))
  [:head] (html/do->
           (html/append (css-links stylesheets))
           (html/append (apply foptimus/css-bundles req css-bundles))
           (html/append (font-links fonts)))
  [:body] (html/do->
           (maybe-prepend navbar)
           (maybe-append (json-objects json))
           (html/append
            (html/html
             (concat
              (js-links scripts)
              (foptimus/js-bundles req (str app-name ".js"))
              [[:script (format "window.onload=function(){%s.core.run();}" app-name)]])))
           (maybe-append (when chatlio? chatlio)))
  [:#app] (if content
            (html/substitute content)
            (html/set-attr :id app-name))
  [:img] #(update-in % [:attrs :src] (foptimus/cache-bust-images req asset-path)))
