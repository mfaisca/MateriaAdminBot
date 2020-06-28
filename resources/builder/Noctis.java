package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Noctis extends _Default{
	public Noctis() {
		super("Noctis", "noct");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{4325, 4326};
				break;
			case HP:
				ids = new int[]{4327, 4328};
				break;
			case S1:
				ids = new int[]{4315, 4316};
				break;
			case S2:
				ids = new int[]{4323, 4324};
				break;
			case EX:
				ids = new int[]{7302, 7300};
				break;
			case AA:
				ids = new int[]{4068};
				break;
			case LD:
				ids = new int[]{9284};
				break;
			case BT:
				ids = new int[]{9285};
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