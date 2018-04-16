JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        MinHeap.java \
        RBTree.java \
        Process.java \
        jobscheduler.java 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class output_file.txt

