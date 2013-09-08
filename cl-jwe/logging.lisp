;;;logging.lisp - a logger for cl-jwe
;;;Written by Keith Jens Carl Johnson, 2013

(in-package :cl-jwe)

(defparameter --LOG-LEVEL-- 0)
(defparameter --LOG-STREAM-- t)

(defun |#$-reader| (stream sub-char numarg)
  (declare (ignore sub-char))
  (if (null numarg) (setf numarg 0))
  (when (> numarg --LOG-LEVEL--)
    (log-string (read stream t nil t))))

(set-dispatch-macro-character #\# #\$ #'|#$-reader|)

(defun log-string (string)
  (multiple-value-bind 
	(second minute hour date month year) 
      (get-decoded-time)   
    (format t "~2,'0d:~2,'0d:~2,'0d-~d/~2,'0d/~d:=> ~a~%"
	    hour
	    minute
	    second
	    month
	    date
	    year
	    string)))