package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Aranea extends _Default{
	public Aranea() {
		super("Aranea", "ara", "ara", "ara", "ara", "ara", "ara", "ara", "ara");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{9288};
				break;
			case HP:
				ids = new int[]{9289};
				break;
			case S1:
				ids = new int[]{8925, 8924};
				break;
			case S2:
				ids = new int[]{8933, 8932};
				break;
			case EX:
				ids = new int[]{8939, 8938};
				break;
			case AA:
				ids = new int[]{8949};
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