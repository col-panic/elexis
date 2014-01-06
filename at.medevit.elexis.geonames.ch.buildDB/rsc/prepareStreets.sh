#!/bin/bash
iconv -f LATIN1 -t UTF-8 ${1} > ${1}.FIXED
