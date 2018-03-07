package factory.domain;

public interface Views {
	public interface Common{}

	public interface Formation extends Common{}
	public interface Stagiaire extends Common{}

	public interface FormationWithStagiaires extends Formation{}
}
