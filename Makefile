INCLUDE_FILES = $(wildcard *.md sites/*.md images/*.png vars.yaml)
pandoc =  /home/fricc/.local/bin/pandoc -s --toc -F include-filter-exe -F venv/bin/pandoc-mustache
docx_outputs = output/docx/pf.docx output/docx/ld.docx output/docx/cx.docx output/docx/speech.docx
html_outputs = output/html/pf.html output/html/ld.html output/html/cx.html output/html/speech.html

render: $(docx_outputs) $(html_outputs)

$(docx_outputs): output/docx/%.docx: events/%.md $(INCLUDE_FILES) output/docx
	$(pandoc) $< --reference-doc reference.docx -o $@

$(html_outputs): output/html/%.html: events/%.md $(INCLUDE_FILES) output/html 
	$(pandoc) $< -o $@

output/html:
	mkdir -p $@

output/docx:
	mkdir -p $@

clean:
	rm -r output

.PHONY: clean
