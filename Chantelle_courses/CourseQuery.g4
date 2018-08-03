grammar CourseQuery;

c           : Subject Code Operation Code;
Subject     : [A-Z]+;
Code        : [0-9]+;
Operation   : ('-');
