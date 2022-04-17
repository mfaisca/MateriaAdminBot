package Units;
import com.materiabot.GameElements.*;
import org.plugface.core.annotations.Plugin;

@Plugin(name = "Unit.Enna")
public class Enna extends Unit{
	public Enna() { super("Enna Kros"); }
	
	@Override
	public String getName() {
		return "Enna Kros";
	}
	
	@Override
	public String getPluginName() {
		return this.getClass().getSimpleName();
	}
}