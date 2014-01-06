#!/bin/bash
iconv -f LATIN1 -t UTF-8 ${1} | sort -k 1 -nt '	' > ${1}.FIXED
