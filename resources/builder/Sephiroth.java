package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Sephiroth extends _Default{
	public Sephiroth() {
		super("Sephiroth", "seph", "seph", "seph", "seph", "seph", "seph", "seph", "seph", "seph", "seph", "seph", "seph", "seph", "seph", "seph", "seph", "seph", "seph", "seph", "seph", "seph", "seph", "seph", "seph", "seph", "seph", "seph", "seph", "seph", "seph", "seph", "seph");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{1799};
				break;
			case HP:
				ids = new int[]{6194};
				break;
			case S1:
				ids = new int[]{3668};
				break;
			case S2:
				ids = new int[]{3674};
				break;
			case EX:
				ids = new int[]{6193};
				break;
			case AA:
				ids = new int[]{6167};
				break;
			case LD:
				ids = new int[]{10009};
				break;
			case BT:
				ids = new int[]{10010};
				break;
			case CA:
				ids = new int[]{};
				break;
		}
		if(ids.length == 0)
			return super.getAbility(type);
		else
			return IntStream.of(ids).boxed().map(i -> this.getSpecificAbility(i)).collect(Collectors.toList());
	}
}