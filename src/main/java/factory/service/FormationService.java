package factory.service;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.time.temporal.TemporalAdjusters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import factory.domain.Formateur;
import factory.domain.Formation;
import factory.domain.Module;
import factory.repository.FormationRepository;
import factory.repository.search.FormationSearchRepository;
import factory.domain.Views;
import com.fasterxml.jackson.annotation.JsonView;


/**
 * Service Implementation for managing Formation.
 */
@Service
@Transactional
public class FormationService {

	private final Logger log = LoggerFactory.getLogger(FormationService.class);

	private final FormationRepository formationRepository;

	private final FormationSearchRepository formationSearchRepository;

	public FormationService(FormationRepository formationRepository,
			FormationSearchRepository formationSearchRepository) {
		this.formationRepository = formationRepository;
		this.formationSearchRepository = formationSearchRepository;
	}

	/**
	 * Save a formation.
	 *
	 * @param formation
	 *            the entity to save
	 * @return the persisted entity
	 */
	public Formation save(Formation formation) {
		log.debug("Request to save Formation : {}", formation);
		Formation result = formationRepository.save(formation);
		formationSearchRepository.save(result);
		return result;
	}

	/**
	 * Get all the formations.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@JsonView(Views.FormationWithStagiaires.class)
	@Transactional(readOnly = true)
	public Page<Formation> findAll(Pageable pageable) {
		log.debug("Request to get all Formations");
		return formationRepository.findAll(pageable);
	}

	/**
	 * Get one formation by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@JsonView(Views.FormationWithStagiaires.class)
	@Transactional(readOnly = true)
	public Formation findOne(Long id) {
		log.debug("Request to get Formation : {}", id);
		return formationRepository.findOneWithEagerRelationships(id);
	}

	/**
	 * Delete the formation by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	public void delete(Long id) {
		log.debug("Request to delete Formation : {}", id);
		formationRepository.delete(id);
		formationSearchRepository.delete(id);
	}

	/**
	 * Search for the formation corresponding to the query.
	 *
	 * @param query
	 *            the query of the search
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<Formation> search(String query, Pageable pageable) {
		log.debug("Request to search for a page of Formations for query {}", query);
		Page<Formation> result = formationSearchRepository.search(queryStringQuery(query), pageable);
		return result;
	}

	public File createPdf2(Formation formation) throws IOException, DocumentException {
		Document document = new Document();

		PdfWriter.getInstance(document, new FileOutputStream("temp.pdf"));
		document.open();

		PdfPTable table = new PdfPTable(3);
		PdfPTable table2 = new PdfPTable(4);

		Stream.of("column header 1", "column header 2", "column header 3").forEach(columnTitle -> {
			PdfPCell header = new PdfPCell();
			header.setBackgroundColor(BaseColor.LIGHT_GRAY);
			header.setBorderWidth(2);
			header.setPhrase(new Phrase(columnTitle));
			table.addCell(header);
		});

		table.addCell("row 1, col 1");
		table.addCell("row 1, col 2");
		table.addCell("row 1, col 3");
		PdfPCell cell = new PdfPCell(new Phrase(" 1,1 "));
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(" 1,2 "));
		table.addCell(cell);
		PdfPCell cell23 = new PdfPCell(new Phrase("1,3 & 2,3"));
		cell23.setRotation(90);
		cell23.setRowspan(10);
		table.addCell(cell23);
		cell = new PdfPCell(new Phrase(" 2,1 "));
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(" 2,2 "));
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(" 3,1 "));
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(" 3,2 "));
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(" 4,1 "));
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(" 4,2 "));
		table.addCell(cell);
		document.add(table);

		Chunk chunk = new Chunk("Hello World");
		Paragraph p = new Paragraph(chunk);
		document.add(p);

		table2.addCell("row 1, col 1");
		table2.addCell("row 1, col 2");
		table2.addCell("row 1, col 3");
		table2.addCell("row 1, col 4");
		PdfPCell cell2 = new PdfPCell(new Phrase("col 1 (10 lines)"));
		cell2.setRowspan(10);
		table2.addCell(cell2);
		table2.addCell("row 2, col 2");
		table2.addCell("row 2, col 3");
		cell2 = new PdfPCell(new Phrase("col 4 (10 lines)"));
		cell2.setRowspan(10);
		table2.addCell(cell2);
		cell2 = new PdfPCell(new Phrase("col 2,3 (5 lines)"));
		cell2.setColspan(2);
		table2.addCell(cell2);
		cell2 = new PdfPCell(new Phrase("col 2,3 (5 lines)"));
		cell2.setRowspan(5);
		table2.addCell(cell2);
		cell2 = new PdfPCell(new Phrase("col 2,3"));
		table2.addCell(cell2);
		cell2 = new PdfPCell(new Phrase("col 2,3"));
		table2.addCell(cell2);
		cell2 = new PdfPCell(new Phrase("col 2,3"));
		table2.addCell(cell2);
		cell2 = new PdfPCell(new Phrase("col 2,3"));
		table2.addCell(cell2);
		cell2 = new PdfPCell(new Phrase("col 2,3"));
		table2.addCell(cell2);
		cell2 = new PdfPCell(new Phrase("col 2,3"));
		table2.addCell(cell2);
		cell2 = new PdfPCell(new Phrase("col 2,3"));
		table2.addCell(cell2);
		cell2 = new PdfPCell(new Phrase("col 2,3"));
		table2.addCell(cell2);
		cell2 = new PdfPCell(new Phrase("col 2,3"));
		table2.addCell(cell2);
		cell2 = new PdfPCell(new Phrase("col 2,3"));
		table2.addCell(cell2);

		document.add(table2);

		chunk = new Chunk("Hello World");
		p = new Paragraph(chunk);
		document.add(p);

		PdfPTable table3 = new PdfPTable(3);
		// we add a cell with colspan 3
		cell = new PdfPCell(new Phrase("Cell with colspan 3"));
		cell.setColspan(3);
		table3.addCell(cell);
		// now we add a cell with rowspan 2
		cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
		cell.setRowspan(2);
		table3.addCell(cell);
		// we add the four remaining cells with addCell()
		table3.addCell("row 1; cell 1");
		table3.addCell("row 1; cell 2");
		table3.addCell("row 2; cell 1");
		table3.addCell("row 2; cell 2");
		document.add(table3);
		document.close();

		File file = new File("temp.pdf");
		return file;
	}

	public File createPdf(Formation formation) throws IOException, DocumentException {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream("src\\main\\webapp\\content\\temp.pdf"));
		document.open();

		PdfPTable table = new PdfPTable(4);
		PdfPCell title = new PdfPCell();
		Phrase titre = new Phrase("formation: " + formation.getNom());
		titre.setFont(FontFactory.getFont(FontFactory.TIMES_ROMAN, 18f));
		
		title.setPhrase(titre);
		title.setColspan(4);
		title.setFixedHeight(40f);;
		title.setVerticalAlignment(Element.ALIGN_CENTER);
		title.setHorizontalAlignment(Element.ALIGN_CENTER);
		title.setBorderWidth(2);
		table.addCell(title);

		Stream.of("Mois", "Date", "Module", "Formateur").forEach(columnTitle -> {
			PdfPCell header = new PdfPCell();
			header.setBackgroundColor(BaseColor.LIGHT_GRAY);
			header.setBorderWidth(2);
			header.setPhrase(new Phrase(columnTitle));
			table.addCell(header);
		});

		LocalDate datedebut = formation.getDateDebutForm();
		LocalDate datefin = formation.getDateFinForm();
		LocalDate date = datedebut;
		List<Formateur> formateurs = new ArrayList<Formateur>();
		formateurs.addAll(formation.getFormateurs());
		List<Module> modules = new ArrayList<Module>();
		modules.addAll(formation.getModules());
		PdfPCell cell = new PdfPCell();
		long daysBetween = ChronoUnit.DAYS.between(datedebut, datefin);
		int dureemodule = 0;
		int nbmodule = 0;
		int spanrem = 0;
		boolean addform = false;
		String month = null;
		Module m = new Module();

		for (int i = 0; i < daysBetween; i++) {

			for (int col = 0; col < 4; col++) {
				switch (col) {
				case 0:
					if (month != date.getMonth().toString()) {
						month = date.getMonth().toString();
						LocalDate temp = date.withDayOfMonth(date.lengthOfMonth());
						int dayfinm = (int) ChronoUnit.DAYS.between(date, temp);
						cell = new PdfPCell(new Phrase(month));
						cell.setRowspan(dayfinm + 1);
						cell.setRotation(90);
						cell.setVerticalAlignment(Element.ALIGN_CENTER);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						table.addCell(cell);
					}
					break;

				case 1:
					String day = date.getDayOfWeek().toString() + " " + date.getDayOfMonth();
					cell = new PdfPCell(new Phrase(day));
					if (date.getDayOfWeek() == DayOfWeek.SATURDAY
							|| date.getDayOfWeek() == DayOfWeek.SUNDAY) {
						cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
					}
					table.addCell(cell);
					date = date.plusDays(1);
					break;

				case 2:
					
					if (dureemodule == 0) {
						if (nbmodule < modules.size()) {
							m = modules.get(nbmodule);
						} else {
							m = null;
							cell = new PdfPCell(new Phrase("pas de modules supplémentaires"));
					if (date.minusDays(1).getDayOfWeek() != DayOfWeek.SATURDAY
							&& date.minusDays(1).getDayOfWeek() != DayOfWeek.SUNDAY) {
						if (dureemodule == 0) {
							if (nbmodule < modules.size()) {
								m = modules.get(nbmodule);
							} else {
								m = null;
								cell = new PdfPCell(new Phrase("pas de modules supplémentaires"));
								table.addCell(cell);
								addform = true;
								break;
							}
							dureemodule = m.getDuree().intValue();
							cell = new PdfPCell(new Phrase(m.getTitre()));
							LocalDate datemod = date.with(DayOfWeek.SATURDAY);
							int span = (int) ChronoUnit.DAYS.between(date.minusDays(1), datemod);
							cell.setRowspan(span);

							BaseColor bc = new BaseColor(m.getCouleur().getR(), m.getCouleur().getG(),
									m.getCouleur().getB());
							cell.setBackgroundColor(bc);
							table.addCell(cell);
							spanrem = dureemodule - span;
							dureemodule--;
							nbmodule++;
							addform = true;
							break;
						} else if (date.minusDays(1).getDayOfWeek() == DayOfWeek.MONDAY
								&& (dureemodule + 1) != m.getDuree().intValue() && spanrem != 0) {
							cell = new PdfPCell(new Phrase(m.getTitre()));
							LocalDate datemod = date.with(DayOfWeek.SATURDAY);
							int span;
							if (spanrem > 5) {
								span = (int) ChronoUnit.DAYS.between(date.minusDays(1), datemod);
							} else {
								span = spanrem;
							}
							BaseColor bc = new BaseColor(m.getCouleur().getR(), m.getCouleur().getG(),
									m.getCouleur().getB());
							cell.setBackgroundColor(bc);
							cell.setRowspan(span);
							table.addCell(cell);
							spanrem = spanrem - span;
							dureemodule--;
						} else {
							dureemodule--;
							addform = false;
						}
						dureemodule = m.getDuree().intValue();
						cell = new PdfPCell(new Phrase(m.getTitre()));
						cell.setRowspan(dureemodule);
						table.addCell(cell);
						dureemodule--;
						nbmodule++;
						addform = true;
					} else {
						dureemodule--;
						addform = false;
						cell = new PdfPCell(new Phrase(""));
						cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
						table.addCell(cell);
					}
					break;

				case 3:

					if (addform) {
						if (m == null) {
							cell = new PdfPCell(new Phrase("please set module"));
							table.addCell(cell);
							break;
						}
						for (int k = 0; k < formateurs.size(); k++) {
							if (m.getFormateurs().contains(formateurs.get(k))) {
								cell = new PdfPCell(new Phrase(formateurs.get(k).getUser().getFirstName() + " "
										+ formateurs.get(k).getUser().getLastName()));

								LocalDate debutform = date.minusDays(1);
								LocalDate finform = debutform.plusDays(dureemodule + 1);

								int spanf = dureemodule + 1;
								long between = ChronoUnit.DAYS.between(debutform, finform);
								between += 2;
								for (int l = 0; l <= between; l++) {
									if (debutform.plusDays(l).getDayOfWeek() == DayOfWeek.SATURDAY
											|| debutform.plusDays(l).getDayOfWeek() == DayOfWeek.SUNDAY) {
										spanf++;
									}
								}
								cell.setVerticalAlignment(Element.ALIGN_CENTER);
								cell.setHorizontalAlignment(Element.ALIGN_CENTER);
								cell.setRotation(270);
								cell.setRowspan(dureemodule + 1);
								cell.setRowspan(spanf);
								table.addCell(cell);
								break;
							} else if (k == formateurs.size() - 1) {
								cell = new PdfPCell(new Phrase("pas de formateur"));
								cell.setVerticalAlignment(Element.ALIGN_CENTER);
								cell.setHorizontalAlignment(Element.ALIGN_CENTER);
								cell.setRotation(270);
								cell.setRowspan(dureemodule + 1);
								table.addCell(cell);
								break;
							}
						}

					}
					break;
				}
			}

		}

		document.add(table);
		document.close();

		File file = new File("src\\main\\webapp\\content\\temp.pdf");
		return file;
	}
}
