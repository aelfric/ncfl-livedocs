files := speech.md pf.md ld.md cx.md

render:
	for input in $(files); do\
		/home/fricc/.local/bin/pandoc -s --toc -F include-filter-exe -F pandoc-mustache -F sms-links $${input} -o output/$${input}.html;\
	done 
