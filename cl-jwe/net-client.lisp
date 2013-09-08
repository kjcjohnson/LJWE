;;;net-client.lisp - a network client
;;;Written by Keith Jens Carl Johnson, 2013

(defconstant --PORT-- 7282)
(defconstant --HOSTNAME-- "localhost")

(in-package :cl-jwe)

(defun read-string (sock)
  (coerce
   (loop with ch while (setf ch (read-char-no-hang (usocket:socket-stream sock))) collecting ch) 'string))

(defun loop-and-listen (sock)
  (loop until (listen (usocket:socket-stream sock)) doing (+ 2 3)) ;idle op
)

(defun connect-to-server ()
  (let ((sock nil)
	(stri nil))

    (handler-case (setf sock (usocket:socket-connect --HOSTNAME-- --PORT--))
      (t (nio) (declare (ignore nio))
	 #1$"Error connecting to server"
	 (return-from connect-to-server nil)))

    (loop-and-listen sock)
    (setf stri (read-string sock))
    (log-string (format nil "String read: ~A" stri))

    (if (string= (coerce '(#\Stx #\Enq #\Etx) 'string) stri)
	t
	(return-from connect-to-server nil))

    (format (usocket:socket-stream sock) "~a" (coerce '(#\Stx #\Ack #\Etx) 'string))
    (force-output (usocket:socket-stream sock))
    (loop-and-listen sock)
    (read-string sock)
    sock))