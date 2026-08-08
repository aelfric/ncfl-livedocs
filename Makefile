INCLUDE_FILES = $(wildcard *.md sites/*.md images/*.png vars.yaml)
pandoc =  /home/fricc/.local/bin/pandoc -s --toc -F include-filter-exe -F venv/bin/pandoc-mustache
docx_outputs = output/docx/pf.docx output/docx/ld.docx output/docx/cx.docx output/docx/speech.docx output/docx/congress.docx
html_outputs = output/html/pf.html output/html/ld.html output/html/cx.html output/html/speech.html output/html/congress.html
md_outputs = output/md/pf.md output/md/ld.md output/md/cx.md output/md/speech.md output/md/congress.md

render: $(docx_outputs) $(html_outputs) $(md_outputs)

schedule: web-schedule.md
	$(pandoc) web-schedule.md -o ./output/html/web-schedule.html

$(docx_outputs): output/docx/%.docx: events/%.md $(INCLUDE_FILES) output/docx
	$(pandoc) $< --reference-doc reference.docx -o $@

$(html_outputs): output/html/%.html: events/%.md $(INCLUDE_FILES) output/html 
	$(pandoc) $< -o $@
	
$(md_outputs): output/md/%.md: events/%.md $(INCLUDE_FILES) output/md
	$(pandoc) $< -o $@

output/html:
	mkdir -p $@

output/docx:
	mkdir -p $@

output/md:
	mkdir -p $@

clean:
	rm -r output

.PHONY: clean
