;;;; -*- Mode: Lisp; Syntax: ANSI-Common-Lisp; Base: 10 -*-
;;; ^just in case emacs doesn't like this file.

;;;cl-jwe.asd
;;;Written by Keith Jens Carl Johnson, 2013

;;;This file is loaded by ASDF to compile the cl-jwe system.

;;;define an appropriate package:
(defpackage "cl-jwe-asd"
  (:use :cl :asdf))
;;;and switch:
(in-package "cl-jwe-asd")

(defsystem cl-jwe
    :name "cl-jwe"
    :author "Keith Jens Carl Johnson"
    :description "Java windowing environment"
    :long-description "A Lisp connection to the Java windowing toolkits"
    :components ((:file "package")
		 (:file "logging"
			:depends-on ("package"))
		 (:file "net-client"
			:depends-on ("package"
				     "logging")))
    :depends-on ("usocket"))
