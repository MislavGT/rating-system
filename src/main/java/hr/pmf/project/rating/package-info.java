/**
 * <p>
 * Paket <b> hr.pmf.project.rating </b> nudi
 * mogućnost dodjeljivanja ocjena natjecateljima
 * na temelju njihovih performansi. Nadalje,
 * nudi mogućnost predviđanja budućih natjecanja
 * na temelju danih ocjena. Koristi statističke i
 * numeričke metode za svoje izračune. Cijeli
 * sustav predvođen je klasom <b> Event </b>. 
 * Međurezultati spremaju se u SQLite bazu. 
 * Sustav se koristi kroz korisničko sučelje temeljeno 
 * na kombinaciji JavaFX i Java Swing alata. Oni nam 
 * nude i neke vizualizacije rezultata. Izračuni su 
 * <b> paralelizirani </b>, a za numerički dio se 
 * koristi poziv <b> nativnih </b> C++ funkcija kroz JNI.
 * </p>
 */

package hr.pmf.project.rating;
