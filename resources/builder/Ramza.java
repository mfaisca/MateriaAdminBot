package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Ramza extends _Default{
	public Ramza() {
		super("Ramza", "beoulve", "beoulve");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{8103, 8104, 11019, 11020, 10899};
				break;
			case HP:
				ids = new int[]{8100, 8101, 8102};
				break;
			case S1:
				ids = new int[]{4406};
				break;
			case S2:
				ids = new int[]{4567};
				break;
			case EX:
				ids = new int[]{7918, 7919, 7920, 7921};
				break;
			case AA:
				ids = new int[]{4367};
				break;
			case LD:
				ids = new int[]{9202};
				break;
			case BT:
				ids = new int[]{10812};
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