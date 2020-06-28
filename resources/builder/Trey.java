package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Trey extends _Default{
	public Trey() {
		super("Trey", "tray");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{8738};
				break;
			case HP:
				ids = new int[]{8739, 8741, 8740};
				break;
			case S1:
				ids = new int[]{8042, 8044, 8043};
				break;
			case S2:
				ids = new int[]{8054, 8056, 8055};
				break;
			case EX:
				ids = new int[]{8733};
				break;
			case AA:
				ids = new int[]{8065};
				break;
			case LD:
				ids = new int[]{};
				break;
			case BT:
				ids = new int[]{};
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